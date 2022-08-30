package com.spaceobj.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.bo.LoginOrRegisterBo;
import com.spaceobj.user.constent.KafKaTopics;
import com.spaceobj.user.constent.OperationType;
import com.spaceobj.user.constent.Regexes;
import com.spaceobj.user.constent.Resource;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.service.kafka.KafkaSender;
import com.spaceobj.user.utils.EmailVerifyCode;
import com.spaceobj.user.utils.FileUtil;
import com.spaceobj.user.utils.ReceiveEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.net.util.IPAddressUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
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

  @Override
  public SaResult loginOrRegister(LoginOrRegisterBo loginOrRegisterBo) {

    // 校验ip是否合法
    String requestIp = getIpAddress();
    if (!isIpAddressCheck(requestIp) || !requestIp.equals(loginOrRegisterBo.getIp())) {
      redisTemplate.opsForValue().set(requestIp, 10, 1, TimeUnit.DAYS);
      return SaResult.error("非法操作，设备已锁定");
    }
    // 校验邮箱
    if (!Pattern.matches(Regexes.REGEX_EMAIL, loginOrRegisterBo.getEmail())) {
      return SaResult.error("邮箱格式错误");
    }
    // 校验密码
    if (!Pattern.matches(Regexes.REGEX_PASSWORD, loginOrRegisterBo.getPassword())) {
      return SaResult.error("密码格式错误");
    }
    // 校验电话号码
    if (!Pattern.matches(Regexes.REGEX_PHONE, loginOrRegisterBo.getPhoneNumber())) {
      return SaResult.error("电话格式错误");
    }

    try {

      if (loginOrRegisterBo.getOperateType().equals(OperationType.LOGIN)) {
        if (redisTemplate.hasKey(loginOrRegisterBo.getEmail())) {
          SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(loginOrRegisterBo.getEmail());
          if (sysUser.getPassword().equals(loginOrRegisterBo.getPassword())) {
            sysUser.setIp(loginOrRegisterBo.getIp());
            sysUser.setIpTerritory(loginOrRegisterBo.getIpTerritory());
            sysUser.setDeviceType(loginOrRegisterBo.getDeviceType());
            // 更新用户当前登录信息
            kafkaSender.send(sysUser, KafKaTopics.USER_UPDATE);
            StpUtil.login(loginOrRegisterBo.getEmail());
            return SaResult.ok("登录成功").setData(sysUser);
          } else {
            return SaResult.error("密码不正确");
          }
        } else {
          return SaResult.error("账号错误或者未注册");
        }

      } else if (loginOrRegisterBo.getOperateType().equals(OperationType.ADD)) {
        // 用户注册校验，是否已经创建过
        if (redisTemplate.hasKey(loginOrRegisterBo.getEmail())) {
          return SaResult.error("请勿重复注册");
        }
        SysUser sysUser =
            SysUser.builder()
                .userId(UUID.randomUUID().toString())
                .inviteUserId(loginOrRegisterBo.getInviteUserId())
                .account(loginOrRegisterBo.getEmail())
                .password(loginOrRegisterBo.getPassword())
                .ip(loginOrRegisterBo.getIp())
                .phoneNumber(loginOrRegisterBo.getPhoneNumber())
                .ipTerritory(loginOrRegisterBo.getIpTerritory())
                .deviceType(loginOrRegisterBo.getDeviceType())
                .build();
        // 注册用户存储到Redis
        redisTemplate.opsForValue().set(loginOrRegisterBo.getEmail(), sysUser);
        // 消息队列通知MySQL
        kafkaSender.send(sysUser, KafKaTopics.USER_REGISTER);
        StpUtil.login(loginOrRegisterBo.getEmail());
        return SaResult.ok("注册成功，正在跳转");
      } else {
        // 逻辑参数校验错误
        return SaResult.error("请求参数错误");
      }
    } catch (RuntimeException e) {
      LOG.error("loginOrRegister failed", e.getMessage());
      return SaResult.error("请求参数错误");
    }
  }

  @Override
  public SaResult loginOut() {
    StpUtil.logout();
    return SaResult.ok();
  }

  @Override
  public SaResult getUserInfo() {
    String account = null;
    SysUser sysUser = null;
    try {
      account = (String) StpUtil.getLoginId();
      sysUser = (SysUser) redisTemplate.opsForValue().get(account);
      return SaResult.ok().setData(sysUser);
    } catch (RuntimeException e) {
      LOG.error("getUserInfo failed", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult updateUserInfo(SysUser user) {

    try {

      // 校验ip是否合法
      String requestIp = getIpAddress();
      if (!isIpAddressCheck(requestIp) || !requestIp.equals(user.getIp())) {
        redisTemplate.opsForValue().set(requestIp, 10, 1, TimeUnit.DAYS);
        return SaResult.error("非法操作，设备已锁定");
      }
      // 校验当前登录用户是否是和修改用户一样
      String account = (String) StpUtil.getLoginId();
      if (!account.equals(user.getAccount())) {
        // 先封禁账号，然后踢下线
        StpUtil.disable(account, -1);
        StpUtil.kickout(account);
        redisTemplate.opsForValue().set(requestIp, 10, 1, TimeUnit.DAYS);
        return SaResult.error("非法操作，账号已封禁、设备已锁定");
      }

      // 昵称、手机号，其余设置为空
      // 校验电话号码
      if (!Pattern.matches(Regexes.REGEX_PHONE, user.getPhoneNumber())) {
        return SaResult.error("电话格式错误");
      }
      // 校验昵称
      if (!Pattern.matches(Regexes.NICKNAME, user.getNickName())) {
        return SaResult.error("昵称格式错误");
      }

      // 如果剩余修改的次数小于等于0，那么回复修改失败
      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(account);
      if (sysUser.getUserInfoEditStatus() == 1) {
        SaResult.error("修改中，请稍后");
      }
      if (sysUser.getEditInfoTimes() <= 0) {
        return SaResult.error("本月剩余修改次数为0，下个月再修改吧！");
      }

      // 设置为正在修改状态
      sysUser.setUserInfoEditStatus(1);
      // 修改缓存中的用户信息为修改中
      redisTemplate.opsForValue().set(account, sysUser);
      // 修改其它属性
      sysUser.setPhotoUrl(user.getPhotoUrl());
      sysUser.setNickName(user.getNickName());
      sysUser.setPhoneNumber(user.getPhoneNumber());
      // 发送到kafka,如果MySQL修改成功，那么刷新缓存
      kafkaSender.send(sysUser, KafKaTopics.USER_UPDATE);
      return SaResult.ok("提交成功");
    } catch (RuntimeException e) {
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
        return SaResult.error("剩余修改次数为0，明天再来吧");
      }
      // 生成邮箱验证码
      String getEmailCodeVerify = EmailVerifyCode.getVerifyCode();
      // 设置邮件的标题，内容，收件人
      ReceiveEmail receiveEmail =
          ReceiveEmail.builder()
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
      redisTemplate.opsForValue().set(account, sysUser);
      return SaResult.ok("发送成功");
    } catch (RuntimeException e) {
      LOG.error("Error send failed", e.getMessage());
      return SaResult.error("发送失败");
    }
  }

  @Override
  public SaResult resetPassword(String account, String emailCode, String newPassword) {
    try {
      // 验证当前登录的用户是否和传递过来的账号一致，如果不一致，封号，踢下线，锁定设备
      String loginId = (String) StpUtil.getLoginId();
      if (!account.equals(loginId)) {
        // 封禁账号
        StpUtil.disable(loginId, -1);
        // 踢下线
        StpUtil.kickout(loginId);
        // 锁定设备
        redisTemplate.opsForValue().set(getIpAddress(), 10, 1, TimeUnit.DAYS);
        return SaResult.error("违规操作，账号已封，设备已锁定");
      }
      // 验证邮箱验证码是否和服务器保存的一致
      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(account);
      String emailCodeFromRedis = sysUser.getEmailCode();
      if (emailCodeFromRedis.equals(emailCode)) {

        // 缓存更新
        sysUser.setPassword(newPassword);
        redisTemplate.opsForValue().set(account, sysUser);
        // kafka通知MySQL修改
        kafkaSender.send(sysUser, KafKaTopics.USER_UPDATE);
        return SaResult.ok("密码修改成功");
      }
      return SaResult.error("密码修改失败");

    } catch (RuntimeException e) {
      LOG.error("resetPassword failed", e.getMessage());
      return SaResult.error("密码重置失败，服务器异常");
    }
  }

  @Override
  public SaResult realName(SysUser user) {

    try {
      // 验证当前登录的用户是否和前端传递过来的账号一致，如果不一致，封号，踢下线，锁定设备
      String loginId = (String) StpUtil.getLoginId();
      if (!user.getAccount().equals(loginId)) {
        // 封禁账号
        StpUtil.disable(loginId, -1);
        // 踢下线
        StpUtil.kickout(loginId);
        // 锁定设备
        redisTemplate.opsForValue().set(getIpAddress(), 10, 1, TimeUnit.DAYS);
        return SaResult.error("违规操作，账号已封，设备已锁定");
      }

      // 从缓存中获取当前登录用户的基本信息
      SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(loginId);

      // 校验当前账户是否是在审核中
      if (sysUser.getRealNameStatus() == 1) {
        return SaResult.error("请勿重复提交");
      }

      // 设置为审核中
      sysUser.setRealNameStatus(1);
      // 设置身份证号码与身份证图片名称
      sysUser.setIdCardNum(user.getIdCardNum());
      sysUser.setIdCardPic(user.getIdCardPic());
      // 缓存设置为审核中
      redisTemplate.opsForValue().set(loginId, sysUser);
      // 消息队列通知MySQL修改用户实名状态，定时任务检测当前实名状态是否大于十个，大于十个审核需求，那么邮件通知管理员审核
      kafkaSender.send(sysUser, KafKaTopics.USER_UPDATE);
      return SaResult.ok("提交成功");

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

  /**
   * IP正则校验
   *
   * @param address
   * @return
   */
  public static boolean isIpAddressCheck(String address) {

    boolean iPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(address);
    boolean iPv6LiteralAddress = IPAddressUtil.isIPv6LiteralAddress(address);
    // ip有可能是v4,也有可能是v6,滿足任何一种都是合法的ip
    if (!(iPv4LiteralAddress || iPv6LiteralAddress)) {
      return false;
    }
    return true;
  }

  /**
   * 获取用户IP
   *
   * @return
   */
  public static String getIpAddress() {

    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return requestAttributes.getRequest().getRemoteHost();
  }
}
