package com.spaceobj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.utils.ResultData;

/**
 * @author zhr_java@163.com
 * @date 2022/7/23 22:27
 */
public interface CustomerUserService extends IService<SysUser> {

  /**
   * 生成用户验证码
   *
   * @param account
   * @return
   */
  ResultData createEmailCode(String account);

  /**
   * 验证码登录/注册
   *
   * @param phoneNumber
   * @param account
   * @param emailCode
   * @return
   */
  ResultData loginOrRegisterByEmailCode(String phoneNumber, String account, String emailCode);

  /**
   * 退出登录
   *
   * @param account
   * @return
   */
  ResultData logout(String account);

  /**
   * 通过用户账号返回用户基本信息
   *
   * @param account
   * @return
   */
  ResultData getUserInfo(String account);

  /**
   * 修改用户信息
   *
   * @param user
   * @return
   */
  ResultData updateUserInfo(SysUser user);
}
