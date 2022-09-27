package com.spaceobj.user.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.domain.SysUser;
import com.spaceobj.user.bo.LoginOrRegisterBo;
import com.spaceobj.user.bo.SysUserBo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhr_java@163.com
 * @date 2022/7/23 22:27
 */
public interface CustomerUserService extends IService<SysUser> {

  /**
   * 登录/注册 先判断是登录还是注册还是修改， 0、登录：需要提供账号、密码，返回token ； 1、注册：验新增用户的账号是否唯一，缓存中没有就先存储缓存中，
   * 消息队列通知MySQL，然后返回给客户token，持久化完毕，消息队列根据账号刷新缓存中的用户ID，如果邀请者Id不为空， 消息队列通知MySQL给邀请者的邀请值加1，随后刷新邀请者的缓存
   *
   * @param loginOrRegisterBo 登录或者注册参数
   * @return
   */
  SaResult loginOrRegister(LoginOrRegisterBo loginOrRegisterBo);

  /**
   * 退出登录，根据当前登录账号的cookie退出登录
   *
   * @return
   */
  SaResult loginOut();

  /**
   * 通过用户当前登录的cookie返回用户基本信息
   *
   * @return
   */
  SaResult getUserInfo();

  /**
   * 修改用户信息，要比较当前登录账户的cookie是否和提交的账号ID一致，
   * 根据用户的账号，先把数据缓存到Redis中，判断用户本月剩余修改次数是否大于0，大于0可以继续修改，先刷新缓存为修改中， 禁止重复提交，然后同步到消息队列，然后刷新缓存，小于0本月禁止修改，
   * 设置定时任务每月一号刷新次数为3
   *
   * <p>校验请求参数：头像、昵称、手机号，其余设置为空
   *
   * <p>补充参数：登录设备、ip、位置信息（监听到消息队列之后添加）、剩余修改次数
   *
   * @param user
   * @return
   */
  SaResult updateUserInfo(SysUserBo user);

  /**
   * 发送验证码
   *
   * <p>校验当前系统中是否存在这个账户,校验剩余发送次数
   *
   * @param account
   * @return
   */
  SaResult sendMailCode(String account);

  /**
   * 通过邮件验证码修改
   *
   * @param sysUserBo 用户实体信息和附加信息
   * @return
   */
  SaResult resetPassword(SysUserBo sysUserBo);

  /**
   * 先缓存到Redis，变成审核状态，消息队列持久化到MySQL，通过任务调度判断超过十个人，则邮件通知管理员审核， 审核后，消息队列通知先持久化，然后刷新缓存，邮件通知用户审核通过
   *
   * <p>校验请求参数：账号、身份证号18位、图片链接、真实姓名，其余设置为空
   *
   * @param user
   * @return
   */
  SaResult realName(SysUserBo user);

  /**
   * 文件上传
   *
   * <p>校验是否登录
   *
   * @param multipartFile
   * @return
   */
  SaResult uploadFile(MultipartFile multipartFile);
}
