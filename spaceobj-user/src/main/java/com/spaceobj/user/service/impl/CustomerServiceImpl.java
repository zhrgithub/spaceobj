package com.spaceobj.user.service.impl;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.config.KafkaSender;
import com.constant.KafKaTopics;
import com.core.constant.OperationType;
import com.core.utils.BeanConvertToTargetUtils;
import com.core.utils.EmailVerifyCode;
import com.core.utils.ExceptionUtil;
import com.redis.common.constant.RedisKey;
import com.redis.common.service.RedisService;
import com.redis.common.service.RedissonService;
import com.spaceobj.user.bo.*;
import com.spaceobj.user.component.QQService;
import com.spaceobj.user.component.WeChatService;
import com.spaceobj.user.constant.Resource;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

  @Autowired private RedisService redisService;

  @Autowired private RedissonService redissonService;

  @Autowired private KafkaSender kafkaSender;

  @Autowired private SysUserMapper sysUserMapper;

  @Autowired private WeChatService weChatService;

  @Autowired private QQService qqService;

  public static final int USER_AUDIT_STATUS = 2;

  @Value("${privateKey}")
  private String privateKey;

  @Override
  public int updateUser(SysUser sysUser) {
    int result = 0;
    try {
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("version", sysUser.getVersion());
      queryWrapper.eq("account", sysUser.getAccount());
      sysUser.setVersion(sysUser.getVersion() + 1);
      result = sysUserMapper.update(sysUser, queryWrapper);
      if(result==1 ){
        redisService.setCacheMapValue(RedisKey.SYS_USER_LIST, sysUser.getAccount(), sysUser);
      }
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return 0;
    }
    return result;
  }

  @Override
  public SaResult loginByWeChat(LoginByWechatBo loginByWeChatBo) {
    try {
      String openId = weChatService.getOpenIdByCode(loginByWeChatBo.getCode());
      if (StringUtils.isEmpty(openId)) {
        return SaResult.error("微信登录失败");
      }
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("open_id", openId);
      SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

      if (ObjectUtils.isEmpty(sysUser)) {
        //  注册
        // 如果邀请用户id不为空，那么给邀请人的邀请值加一
        if (!StringUtils.isEmpty(loginByWeChatBo.getInviteUserId())) {
          SysUser inviteUser = new SysUser();
          inviteUser.setUserId(loginByWeChatBo.getInviteUserId());
          kafkaSender.send(inviteUser, KafKaTopics.INVITER_VALUE_ADD);
        }
        // 创建新用户
        sysUser = SysUser.builder().userId(UUID.randomUUID().toString()).build();
        sysUser.setAccount(openId);
        sysUser.setOpenId(openId);
        sysUser.setIpTerritory(loginByWeChatBo.getIpTerritory());
        StpUtil.login(openId);
        sysUser.setToken(StpUtil.getTokenValue());
        sysUser.setNickName(loginByWeChatBo.getNickName());
        sysUser.setOnlineStatus(1);
        int result = sysUserMapper.insert(sysUser);
        if (result == 0) {
          return SaResult.error("服务器繁忙");
        }
        this.updateCacheByAccount(sysUser.getAccount());
        return SaResult.ok("提交成功").setData(sysUser);
      } else {
        // 账号登录
        //  判断用户是否被封禁
        boolean isDisable = StpUtil.isDisable(sysUser.getAccount());
        if (isDisable) {
          return SaResult.error("账号已被封禁！");
        }
        sysUser.setIpTerritory(loginByWeChatBo.getIpTerritory());
        StpUtil.login(sysUser.getAccount());
        sysUser.setToken(StpUtil.getTokenValue());
        sysUser.setOnlineStatus(1);
        // 更新用户登录位置
        int updateResult = this.updateUser(sysUser);
        if (updateResult == 0) {
          LOG.error("用户登录信息更新失败");
          return SaResult.error("服务器繁忙");
        }
        return SaResult.ok("登录成功").setData(sysUser);
      }
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器繁忙");
    }
  }

  @Override
  public SaResult loginByQQ(LoginByQQBo loginByQQBo) {
    try {
      String qqOpenId = qqService.getOpenIdByCode(loginByQQBo.getCode());
      if (StringUtils.isEmpty(qqOpenId)) {
        return SaResult.error("QQ授权失败");
      }
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("qq_open_id", qqOpenId);
      SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
      // 用户注册
      if (ObjectUtils.isEmpty(sysUser)) {
        //  注册
        // 如果邀请用户id不为空，那么给邀请人的邀请值加一
        if (!StringUtils.isEmpty(loginByQQBo.getInviteUserId())) {
          SysUser inviteUser = new SysUser();
          inviteUser.setUserId(loginByQQBo.getInviteUserId());
          kafkaSender.send(inviteUser, KafKaTopics.INVITER_VALUE_ADD);
        }
        // 创建新用户
        sysUser = SysUser.builder().userId(UUID.randomUUID().toString()).build();
        sysUser.setAccount(qqOpenId);
        sysUser.setQqOpenId(qqOpenId);
        sysUser.setIpTerritory(loginByQQBo.getIpTerritory());
        StpUtil.login(qqOpenId);
        sysUser.setToken(StpUtil.getTokenValue());
        sysUser.setNickName(loginByQQBo.getNickName());
        sysUser.setOnlineStatus(1);
        int result = sysUserMapper.insert(sysUser);
        if (result == 0) {
          return SaResult.error("服务器繁忙");
        }
        this.updateCacheByAccount(sysUser.getAccount());
        return SaResult.ok("登录成功").setData(sysUser);
      } else {
        // 账号登录
        //  判断用户是否被封禁
        boolean isDisable = StpUtil.isDisable(sysUser.getAccount());
        if (isDisable) {
          return SaResult.error("账号已被封禁！");
        }
        sysUser.setIpTerritory(loginByQQBo.getIpTerritory());
        StpUtil.login(sysUser.getAccount());
        sysUser.setToken(StpUtil.getTokenValue());
        sysUser.setOnlineStatus(1);
        // 更新用户登录位置
        int updateResult = this.updateUser(sysUser);
        if (updateResult == 0) {
          LOG.error("用户登录信息更新失败");
          return SaResult.error("服务器繁忙");
        }
        return SaResult.ok("登录成功").setData(sysUser);
      }

    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器繁忙");
    }
  }

  @Override
  public SaResult loginByEmail(LoginByEmailBo loginByEmailBo) {
    // 校验验证码是否存在
    if (!redisService.hasKey(loginByEmailBo.getEmail())) {
      return SaResult.error("验证码已失效");
    }
    // 获取缓存中的验证码
    String emailCodeFromRedis =
        redisService.getCacheObject(loginByEmailBo.getEmail(), String.class);
    if (!loginByEmailBo.getEmailCode().equals(emailCodeFromRedis)) {
      return SaResult.error("验证码错误");
    }
    // 校验用户已经存在，直接登录
    QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
    wrapper.eq("email", loginByEmailBo.getEmail());
    SysUser sysUser = sysUserMapper.selectOne(wrapper);

    StpUtil.login(loginByEmailBo.getEmail());
    String token = StpUtil.getTokenValue();

    if (!ObjectUtils.isEmpty(sysUser)) {
      BeanConvertToTargetUtils.copyNotNullProperties(loginByEmailBo, sysUser);
      sysUser.setToken(token);
      this.updateById(sysUser);
      return SaResult.ok().setData(sysUser);
    }

    // 用户不存在注册新的用户
    SysUser user = new SysUser();
    user.setToken(token);
    BeanConvertToTargetUtils.copyNotNullProperties(loginByEmailBo, user);
    this.sysUserMapper.insert(sysUser);
    // 如果邀请人id不为空，邀请人的邀请值+1
    if (!StringUtils.isEmpty(loginByEmailBo.getInviteUserId())) {
      SysUser inviteUser = new SysUser();
      inviteUser.setUserId(loginByEmailBo.getInviteUserId());
      kafkaSender.send(inviteUser, KafKaTopics.INVITER_VALUE_ADD);
    }
    return SaResult.ok().setData(sysUser);
  }

  @Override
  public SaResult bindWechat(SysUserBo sysUserBo) {
    try {
      // 获取openID
      String openid = weChatService.getOpenIdByCode(sysUserBo.getCode());
      if (StringUtils.isEmpty(openid)) {
        return SaResult.error("openID获取失败");
      }
      // 校验有没有已经绑定账户
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("open_id", openid);
      List<SysUser> sysUserList = sysUserMapper.selectList(queryWrapper);
      if (sysUserList.size() > 0) {
        return SaResult.error("微信已被占用");
      }

      SysUser sysUser = this.getUserInfoByUserId(sysUserBo.getUserId());
      if (!ObjectUtil.isNotNull(sysUser)) {
        return SaResult.error("用户不存在");
      }
      sysUser.setOpenId(openid);
      int result = this.updateUser(sysUser);
      if (result == 0) {
        return SaResult.error("绑定失败，服务器异常");
      }
      return SaResult.ok("绑定成功").setData(sysUser);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器繁忙");
    }
  }

  @Override
  public SysUser getUserInfoByUserId(String userId) {
    QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_id", userId);
    return sysUserMapper.selectOne(queryWrapper);
  }

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
    try {
      // 获取md5格式的密码
      String md5Password = PassWordUtils.passwordToMD5(privateKey, loginOrRegisterBo.getPassword());
      SysUser getUser = getUser(loginOrRegisterBo.getAccount());
      // 判断操作类型
      if (loginOrRegisterBo.getOperateType().equals(OperationType.LOGIN)) {
        // 判断缓存中是否有次账号
        if (ObjectUtils.isEmpty(getUser)) {
          return SaResult.error("用户不存在");
        } else {
          SysUser sysUser = getUser;
          // 校验密码
          if (ObjectUtils.isNull(sysUser.getPassword())) {
            return SaResult.error("用户未设置密码");
          }
          if (sysUser.getPassword().equals(md5Password)) {
            sysUser.setOnlineStatus(1);
            sysUser.setToken(StpUtil.getTokenValue());
            sysUser.setIpTerritory(loginOrRegisterBo.getIpTerritory());
            sysUser.setDeviceType(loginOrRegisterBo.getDeviceType());
            StpUtil.login(sysUser.getAccount());
            sysUser.setToken(StpUtil.getTokenValue());
            // 更新用户登录位置
            int updateResult = this.updateUser(sysUser);
            if (updateResult == 0) {
              LOG.error("用户登录信息更新失败");
              return SaResult.error("服务器繁忙");
            }
            return SaResult.ok("登录成功").setData(sysUser);
          } else {
            return SaResult.error("密码不正确");
          }
        }
      } else if (loginOrRegisterBo.getOperateType().equals(OperationType.ADD)) {
        // 用户注册校验，是否已经创建过
        if (ObjectUtils.isNotEmpty(getUser)) {
          return SaResult.error("请勿重复注册");
        }
        SysUser sysUser = SysUser.builder().userId(UUID.randomUUID().toString()).build();
        if (StringUtils.isEmpty(loginOrRegisterBo.getPhoneNumber())) {
          return SaResult.error("手机号不为空");
        }
        // 校验联系电话
        if (loginOrRegisterBo.getPhoneNumber().length() > 11) {
          return SaResult.error("电话格式错误");
        }
        // 创建一个新的用户对象信息
        sysUser = registerUserObj(loginOrRegisterBo, sysUser, md5Password);
        StpUtil.login(loginOrRegisterBo.getAccount());
        sysUser.setToken(StpUtil.getTokenValue());
        // 新增到MySQL
        int result = sysUserMapper.insert(sysUser);
        if (result == 0) {
          return SaResult.error("注册失败");
        }
        // 刷新缓存
        this.updateCacheByAccount(sysUser.getAccount());
        // 如果邀请人信息不为空，那么给邀请人的邀请值加一
        if (!StringUtils.isEmpty(sysUser.getInviteUserId())) {
          SysUser inviteUser = new SysUser();
          inviteUser.setUserId(sysUser.getInviteUserId());
          kafkaSender.send(inviteUser, KafKaTopics.INVITER_VALUE_ADD);
        }
        return SaResult.ok("注册成功").setData(sysUser);
      }
      return SaResult.error("请求参数错误");
    } catch (SaTokenException e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("密码格式不正确");
    } catch (RuntimeException e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  /** 根据账户刷新缓存信息 */
  private void updateCacheByAccount(String account) {
    QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
    sysUserQueryWrapper.eq("account", account);
    SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
    if (ObjectUtils.isNotEmpty(sysUser)) {
      redisService.setCacheMapValue(RedisKey.SYS_USER_LIST, account, sysUser);
    }
  }

  /**
   * 返回一个注册用户的基本信息
   *
   * @param loginOrRegisterBo
   * @param sysUser
   * @param md5Password
   * @return
   */
  private SysUser registerUserObj(
      LoginOrRegisterBo loginOrRegisterBo, SysUser sysUser, String md5Password) {

    BeanConvertToTargetUtils.copyNotNullProperties(loginOrRegisterBo, sysUser);
    sysUser.setEmail(loginOrRegisterBo.getAccount());
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
    sysUser.setPassword(md5Password);
    sysUser.setVersion(0L);
    return sysUser;
  }

  /**
   * 根据账户获取账户信息，不存在则返回null
   *
   * @param account
   * @return
   */
  private SysUser getUser(String account) {
    SysUser sysUser = null;

    try {
      boolean hasKey = redisService.HExists(RedisKey.SYS_USER_LIST, account);
      // 如果缓存中存在这个hash key，直接返回
      if (hasKey) {
        sysUser = redisService.getCacheMapValue(RedisKey.SYS_USER_LIST, account, SysUser.class);
        return sysUser;
      } else {
        // 添加分布式锁
        boolean flag = redissonService.tryLock(account);
        if (!flag) {
          return null;
        } else {
          //  成功获取到锁，再次从缓存中查询一遍，如果缓存中存在这个hash key，直接返回
          hasKey = redisService.HExists(RedisKey.SYS_USER_LIST, account);
          if (hasKey) {
            sysUser = redisService.getCacheMapValue(RedisKey.SYS_USER_LIST, account, SysUser.class);
            return sysUser;
          }
          // 如果缓存中不存在这个hash key，从数据库中查找
          QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
          queryWrapper.eq("account", account).or().eq("email",account);
          sysUser = sysUserMapper.selectOne(queryWrapper);
          if (!ObjectUtils.isEmpty(sysUser)) {
            redisService.setCacheMapValue(RedisKey.SYS_USER_LIST, sysUser.getAccount(), sysUser);
          }
        }
      }
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return null;
    }
    return sysUser;
  }

  @Override
  public SaResult loginOut() {
    try {
      String loginId = StpUtil.getLoginId().toString();
      // 获取用户列表数据
      SysUser sysUser = getUser(loginId);
      if (ObjectUtils.isEmpty(sysUser)) {
        return SaResult.error("用户不存在");
      }
      StpUtil.logout(loginId);
      sysUser.setOnlineStatus(0);
      int updateResult = updateUser(sysUser);
      if (updateResult == 0) {
        LOG.error("用户登出时，基本信息更新失败");
        return SaResult.error("服务器繁忙");
      }
      return SaResult.ok("登出成功");
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      return SaResult.error("登出失败！服务器异常!");
    }
  }

  @Override
  public SaResult getUserInfo() {
    SysUser sysUser = null;
    try {
      String loginId = StpUtil.getLoginId().toString();
      // 获取用户列表数据
      sysUser = getUser(loginId);
      if (ObjectUtils.isEmpty(sysUser)) {
        return SaResult.error("用户不存在");
      }
      return SaResult.ok("提交成功").setData(sysUser);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult updateUserInfo(SysUserBo user) {
    try {

      // 校验当前登录用户是否是和修改用户一样
      String account = StpUtil.getLoginId().toString();
      // 如果当前账号已经被封禁
      SysUser sysUser = getUser(account);
      if (ObjectUtils.isEmpty(sysUser)) {
        return SaResult.error("用户不存在");
      }
      if (sysUser.getDisableStatus() == 1) {
        return SaResult.error("账号已被封禁，禁止操作！");
      }

      // 如果剩余修改的次数小于等于0，那么回复修改失败
      if (sysUser.getEditInfoTimes() <= 0) {
        return SaResult.error("本月剩余修改次数为0，下个月再修改吧！");
      }

      // 昵称、手机号、邮箱不为空，其余设置为空
      // 校验电话号码
      if (user.getPhoneNumber().length() != 11) {
        return SaResult.error("电话格式错误");
      }

      // 校验邮箱格式
      if (!Pattern.matches(RegexPool.EMAIL, user.getEmail())) {
        return SaResult.error("邮箱格式错误");
      }
      // 校验邮箱是否被占用
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("email", user.getEmail());
      queryWrapper.ne("account", user.getAccount());
      SysUser emailCheckWeight = sysUserMapper.selectOne(queryWrapper);
      if (ObjectUtils.isNotEmpty(emailCheckWeight)) {
        return SaResult.error("邮箱已被占用");
      }

      sysUser.setPhoneNumber(user.getPhoneNumber());
      sysUser.setNickName(user.getNickName());
      sysUser.setPhotoUrl(user.getPhotoUrl());
      sysUser.setIpTerritory(user.getIpTerritory());
      sysUser.setEmail(user.getEmail());

      // 设置修改次数减一
      sysUser.setEditInfoTimes(sysUser.getEditInfoTimes() - 1);
      // 修改用户信息，删除缓存
      int result = updateUser(sysUser);
      if (result == 0) {
        LOG.error("用户基本信息修改失败");
        return SaResult.error("修改失败，稍后提交");
      }
      return SaResult.ok("修改成功").setData(sysUser);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器异常，修改失败");
    }
  }

  @Override
  public SaResult sendMailCode(String account) {
    try {
      SysUser sysUser = null;
      // 校验缓存中是否存在用户基本信息
      sysUser = getUser(account);
      if (ObjectUtils.isEmpty(sysUser)) {
        return SaResult.error("用户不存在");
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
              .receiverEmail(sysUser.getEmail())
              .title("spaceObj")
              .content("邮箱验证码:" + getEmailCodeVerify + "，有效期：3 分钟")
              .build();
      // 缓存邮箱验证码到Redis，有效期3分钟
      redisService.setCacheObject(sysUser.getEmail(), getEmailCodeVerify, 3L, TimeUnit.MINUTES);
      // 邮箱、短信剩余发送次数减一
      sysUser.setSendCodeTimes(sysUser.getSendCodeTimes() - 1);
      // 消息队列通知邮箱服务器发送邮件
      kafkaSender.send(receiveEmail, KafKaTopics.EMAIL_VERIFICATION_CODE);
      // 更新用户发送邮件次数
      int updateResult = updateUser(sysUser);
      if (updateResult == 0) {
        LOG.error("更新用户发送邮件次数失败");
        return SaResult.error("服务器繁忙");
      }
      return SaResult.ok("发送成功");
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      return SaResult.error("发送失败");
    }
  }

  @Override
  public SaResult resetPassword(SysUserBo sysUserBo) {
    try {

      SysUser sysUser = null;
      sysUser = getUser(sysUserBo.getAccount());
      if (ObjectUtils.isEmpty(sysUser)) {
        return SaResult.error("用户不存在");
      }
      // 验证邮箱验证码是否和服务器保存的一致
      if (!redisService.hasKey(sysUser.getEmail())) {
        return SaResult.error("验证码已失效");
      }
      String emailCodeFromRedis = redisService.getCacheObject(sysUser.getEmail(), String.class);
      if (emailCodeFromRedis.equals(sysUserBo.getEmailCode())) {

        // 获取md5格式的密码
        String md5Password = PassWordUtils.passwordToMD5(privateKey, sysUserBo.getNewPassword());
        if (StringUtils.isEmpty(md5Password)) {
          return SaResult.error("数据格式错误");
        }
        // 缓存更新
        sysUser.setPassword(md5Password);
        sysUser.setEmailCode(null);
        // 修改用户数据，并删除缓存
        int result = updateUser(sysUser);
        if (result == 0) {
          LOG.error("修改失败{}", sysUser.getUserId());
          return SaResult.error("服务器繁忙");
        }
        return SaResult.ok("修改成功");
      }
      return SaResult.error("验证码错误");
    } catch (SaTokenException e) {
      ExceptionUtil.exceptionToString(e);
      return SaResult.error("密码格式错误");
    } catch (RuntimeException e) {
      ExceptionUtil.exceptionToString(e);
      return SaResult.error("服务器异常");
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
      SysUser sysUser =
          redisService.getCacheMapValue(RedisKey.SYS_USER_LIST, user.getLoginId(), SysUser.class);

      // 校验当前账户是否是在审核中
      if (sysUser.getRealNameStatus() == USER_AUDIT_STATUS) {
        return SaResult.error("审核中，请勿重复提交");
      }

      // 设置为审核中
      sysUser.setRealNameStatus(2);
      // 设置身份证号码与身份证图片名称
      sysUser.setUsername(user.getUsername());
      sysUser.setIdCardNum(user.getIdCardNum());
      sysUser.setIdCardPic(user.getIdCardPic());
      // 修改用户实名状态
      int result = updateUser(sysUser);
      if (result == 0) {
        LOG.error("实名认证提交失败{}" + sysUser.getUserId());
        return SaResult.error("服务器繁忙");
      }
      return SaResult.ok("提交成功,待审核").setData(sysUser);
    } catch (RuntimeException e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
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
    return SaResult.ok("上传成功").setData(url);
  }
}
