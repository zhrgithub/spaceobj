package com.spaceobj.user.service.impl;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.domain.SysUser;
import com.spaceobj.user.bo.LoginOrRegisterBo;
import com.spaceobj.user.bo.ReceiveEmailBo;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.constant.KafKaTopics;
import com.spaceobj.user.constant.OperationType;
import com.spaceobj.user.constant.Resource;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.service.kafka.KafkaSender;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import com.spaceobj.user.utils.EmailVerifyCode;
import com.spaceobj.user.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author zhr_java@163.com
 * @date 2022/7/23 22:44
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements CustomerUserService {
  private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private KafkaSender kafkaSender;

  @Value("${aseKey}")
  private String key;

  @Override
  public SaResult loginOrRegister(LoginOrRegisterBo loginOrRegisterBo) {
    // 判断用户是否被封禁
    boolean isDisable = StpUtil.isDisable(loginOrRegisterBo.getAccount());
    if (isDisable) {
      return SaResult.error("账号已被封禁！");
    }
    // 校验邮箱
    if (!Pattern.matches(RegexPool.EMAIL, loginOrRegisterBo.getAccount())) {
      return SaResult.error("邮箱格式错误");
    }
    // 校验电话号码
    if (!Pattern.matches(RegexPool.MOBILE, loginOrRegisterBo.getPhoneNumber())) {
      return SaResult.error("电话格式错误");
    }

    try {
      if (loginOrRegisterBo.getOperateType().equals(OperationType.LOGIN)) {
        if (redisTemplate.hasKey(loginOrRegisterBo.getAccount())) {
          SysUser sysUser =
              (SysUser) redisTemplate.opsForValue().get(loginOrRegisterBo.getAccount());
          // 密码解密
          String aesDecryptPassword = SaSecureUtil.aesDecrypt(key, loginOrRegisterBo.getPassword());
          if (sysUser.getPassword().equals(aesDecryptPassword)) {
            StpUtil.login(loginOrRegisterBo.getAccount());
            sysUser.setOnlineStatus(1);
            sysUser.setToken(StpUtil.getTokenValue());
            loginOrRegisterBo.setPassword(null);
            BeanConvertToTargetUtils.copyNotNullProperties(loginOrRegisterBo, sysUser);
            // 更新用户当前登录信息
            kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
            return SaResult.ok("登录成功").setData(sysUser);
          } else {
            return SaResult.error("密码不正确");
          }
        } else {
          // 先刷新缓存然后再次查询
          SysUser sysUser = SysUser.builder().account(loginOrRegisterBo.getAccount()).build();
          kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER_LIST);
          return SaResult.error("系统用户数据同步中，请稍后再试");
        }
      } else if (loginOrRegisterBo.getOperateType().equals(OperationType.ADD)) {
        // 用户注册校验，是否已经创建过
        if (redisTemplate.hasKey(loginOrRegisterBo.getAccount())) {
          return SaResult.error("请勿重复注册");
        }

        SysUser sysUser = SysUser.builder().userId(UUID.randomUUID().toString()).build();
        BeanConvertToTargetUtils.copyNotNullProperties(loginOrRegisterBo, sysUser);

        StpUtil.login(loginOrRegisterBo.getAccount());
        sysUser.setOnlineStatus(1);
        sysUser.setToken(StpUtil.getTokenValue());
        sysUser.setAssistValue(0);
        sysUser.setInvitationValue(0);
        sysUser.setUserInfoEditStatus(0);
        sysUser.setRealNameStatus(0);
        sysUser.setEditInfoTimes(3);
        sysUser.setSendCodeTimes(3);
        sysUser.setReleaseProjectTimes(10);
        sysUser.setProjectHelpTimes(10);
        sysUser.setCreateProjectHelpTimes(10);
        sysUser.setDisableStatus(0);
        // 消息队列通知MySQL
        kafkaSender.send(sysUser, KafKaTopics.ADD_USER);
        return SaResult.ok("注册成功，正在跳转").setData(sysUser);
      } else {
        // 逻辑参数校验错误
        return SaResult.error("请求参数错误");
      }
    } catch (SaTokenException e) {
      return SaResult.error("密码格式不正确");
    } catch (RuntimeException e) {
      e.printStackTrace();
      LOG.error("loginOrRegister failed", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult loginOut(String loginId) {
    try {
      if (StringUtils.isEmpty(loginId)) {
        return SaResult.error("登录id不为空");
      }
      StpUtil.logout(loginId);
      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(loginId);
      if (ObjectUtils.isNull(sysUser)) {
        return SaResult.error("账号不存在");
      }
      sysUser.setOnlineStatus(0);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      return SaResult.ok("登出成功");
    } catch (Exception e) {
      LOG.error("login out error", e.getMessage());
      return SaResult.error("登出失败！服务器异常!");
    }
  }

  @Override
  public SaResult getUserInfo() {
    SysUser sysUser = null;
    try {
      String loginId = StpUtil.getLoginId().toString();
      sysUser = (SysUser) redisTemplate.opsForValue().get(loginId);
      if (ObjectUtils.isNull(sysUser)) {
        return SaResult.error("账号不存在");
      }
      return SaResult.ok().setData(sysUser);
    } catch (RuntimeException e) {
      LOG.error("getUserInfo failed", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult updateUserInfo(SysUserBo user) {
    try {

      // 校验当前登录用户是否是和修改用户一样
      String account = StpUtil.getLoginId().toString();

      // 如果当前账号已经被封禁
      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(account);
      if (sysUser.getDisableStatus() == 1) {
        return SaResult.error("账号已被封禁，禁止操作！");
      }
      // 如果是在修改中，那么禁止重复提交
      if (sysUser.getUserInfoEditStatus() == 1) {
        return SaResult.error("修改中，请稍后！");
      }
      // 如果剩余修改的次数小于等于0，那么回复修改失败
      if (sysUser.getEditInfoTimes() <= 0) {
        return SaResult.error("本月剩余修改次数为0，下个月再修改吧！");
      }

      // 昵称、手机号，其余设置为空
      // 校验电话号码
      if (!Pattern.matches(RegexPool.MOBILE, user.getPhoneNumber())) {
        return SaResult.error("电话格式错误");
      }
      // 校验昵称
      if (!Pattern.matches(RegexPool.GENERAL_WITH_CHINESE, user.getNickName())) {
        return SaResult.error("昵称格式错误");
      }

      sysUser.setPhoneNumber(user.getPhoneNumber());
      sysUser.setNickName(user.getNickName());
      sysUser.setPhotoUrl(user.getPhotoUrl());
      sysUser.setIpTerritory(user.getIpTerritory());

      // 设置为正在修改状态
      sysUser.setUserInfoEditStatus(1);
      // 修改缓存中的用户信息为修改中
      redisTemplate.opsForValue().set(account, sysUser);
      // 设置为修改完毕状态，等MySQL持久化完毕自动变成修改完毕的状态
      sysUser.setUserInfoEditStatus(0);
      // 设置修改次数减一
      sysUser.setEditInfoTimes(sysUser.getEditInfoTimes() - 1);
      // 发送到kafka,如果MySQL修改成功，那么刷新缓存
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      return SaResult.ok("提交成功");
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("updateUserInfo failed", e.getMessage());
      return SaResult.error("服务器异常，修改失败");
    }
  }

  @Override
  public SaResult sendMailCode(String account) {
    try {
      SysUser sysUser = null;
      // 校验缓存中是否存在用户基本信息
      if (redisTemplate.hasKey(account)) {
        sysUser = (SysUser) redisTemplate.opsForValue().get(account);
      } else {
        return SaResult.error("账号不存在");
      }
      // 校验剩余修改次数是否小于0
      if (sysUser.getSendCodeTimes() <= 0) {
        return SaResult.error("验证码发送次数上线，明天再来吧");
      }
      // 生成邮箱验证码
      String getEmailCodeVerify = EmailVerifyCode.getVerifyCode();
      // 设置邮件的标题，内容，收件人
      ReceiveEmailBo receiveEmail =
          ReceiveEmailBo.builder()
              .receiverEmail(account)
              .title("spaceObj")
              .content("邮箱验证码:" + getEmailCodeVerify)
              .build();
      // 重置当前用户的邮箱验证码
      sysUser.setEmailCode(getEmailCodeVerify);
      // 邮箱、短信剩余发送次数减一
      sysUser.setSendCodeTimes(sysUser.getSendCodeTimes() - 1);
      // 消息队列通知邮箱服务器发送邮件
      kafkaSender.send(receiveEmail, KafKaTopics.EMAIL_VERIFICATION_CODE);
      // Redis缓存刷新用户今天剩余邮件、短信发送次数
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      return SaResult.ok("发送成功");
    } catch (Exception e) {
      LOG.error("Error send failed", e.getMessage());
      return SaResult.error("发送失败");
    }
  }

  @Override
  public SaResult resetPassword(SysUserBo sysUserBo) {
    try {

      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(sysUserBo.getAccount());

      // 验证邮箱验证码是否和服务器保存的一致
      String emailCodeFromRedis = sysUser.getEmailCode();
      if (emailCodeFromRedis.equals(sysUserBo.getEmailCode())) {
        // 缓存更新
        String password = SaSecureUtil.aesDecrypt(key, sysUserBo.getNewPassword());
        sysUser.setPassword(password);
        // kafka通知MySQL修改
        kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
        return SaResult.ok("密码修改成功");
      }
      sysUser.setEmailCode(null);
      redisTemplate.opsForValue().set(sysUser.getAccount(), sysUser);
      return SaResult.error("验证码错误，密码修改失败");
    } catch (SaTokenException e) {
      return SaResult.error("密码格式不正确");
    } catch (RuntimeException e) {
      LOG.error("resetPassword failed", e.getMessage());
      return SaResult.error("密码重置失败，服务器异常");
    }
  }

  @Override
  public SaResult realName(SysUserBo user) {

    try {
      // 身份证号校验
      if (!Pattern.matches(RegexPool.CITIZEN_ID, user.getIdCardNum())) {
        return SaResult.error("身份证号不正确");
      }
      // 从缓存中获取当前登录用户的基本信息
      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(user.getLoginId());

      // 校验当前账户是否是在审核中
      if (sysUser.getRealNameStatus() == 2) {
        return SaResult.error("审核中，请勿重复提交");
      }

      // 设置为审核中
      sysUser.setRealNameStatus(2);
      // 设置身份证号码与身份证图片名称
      sysUser.setIdCardNum(user.getIdCardNum());
      sysUser.setIdCardPic(user.getIdCardPic());
      // 消息队列通知MySQL修改用户实名状态，定时任务检测当前实名状态是否大于十个，大于十个审核需求，那么邮件通知管理员审核
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      return SaResult.ok("提交成功,大概需要一到两个工作日审核").setData(sysUser);
    } catch (RuntimeException e) {
      LOG.error("realName failed", e.getMessage());
      return SaResult.error("提交失败，服务器异常");
    }
  }

  @Override
  public SaResult uploadFile(MultipartFile multipartFile) {

    SaResult saResult = FileUtil.uploadImageFile(multipartFile, Resource.IMAGE_FILE_TYPES);

    if (null == saResult.getData()) {
      return saResult;
    }
    String fileName = (String) saResult.getData();
    String url =
        FileUtil.uploadFileTolocalServer(
            Resource.SYS_USER_ID_CARD_DIRECTORY, fileName, multipartFile);

    return SaResult.ok().setData(url);
  }

  @Override
  public SaResult aesEncrypt(String text) {
    String ciphertext = SaSecureUtil.aesEncrypt(key, text);
    return SaResult.ok().setData(ciphertext);
  }
}
