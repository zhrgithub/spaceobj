package com.spaceobj.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.core.utils.BeanConvertToTargetUtils;
import com.spaceobj.user.bo.*;
import com.spaceobj.user.dto.CustomerUserDto;
import com.spaceobj.user.dto.LoginByEmailDto;
import com.spaceobj.user.group.customer.*;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 19:41
 */
@RestController("customerUserController")
@RequestMapping("/customerUser")
public class CustomerUserController {

  @Autowired private CustomerUserService customerUserService;

  @PostMapping("loginOrRegister")
  public SaResult loginOrRegister(
      @Validated(LoginOrRegisterGroup.class) CustomerUserDto customerUserDto) {
    LoginOrRegisterBo loginOrRegisterBo = LoginOrRegisterBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginOrRegisterBo);
    return customerUserService.loginOrRegister(loginOrRegisterBo);
  }

  @PostMapping("loginByWechat")
  public SaResult loginByWechat(@Validated(LoginByWechat.class) CustomerUserDto customerUserDto) {
    LoginByWechatBo loginByWechat = LoginByWechatBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginByWechat);
    return customerUserService.loginByWeChat(loginByWechat);
  }

  @PostMapping("loginByQQ")
  public SaResult loginByQQ(@Validated(LoginByQQ.class) CustomerUserDto customerUserDto) {
    LoginByQQBo loginByQQBo = LoginByQQBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginByQQBo);
    return customerUserService.loginByQQ(loginByQQBo);
  }

  @PostMapping("bindWechat")
  public SaResult bindWechat(@Validated(BindWechat.class) CustomerUserDto customerUserDto) {
    String code = customerUserDto.getCode();
    if (StringUtils.isEmpty(code)) {
      return SaResult.error("授权失败");
    }
    SysUserBo sysUserBo = new SysUserBo();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);
    System.out.println(customerUserDto);
    System.out.println(sysUserBo);
    return customerUserService.bindWechat(sysUserBo);
  }

  @PostMapping("loginOut")
  public SaResult loginOut() {
    return customerUserService.loginOut();
  }

  @PostMapping("getUserInfo")
  public SaResult getUserInfo() {

    return customerUserService.getUserInfo();
  }

  @PostMapping("updateUserInfo")
  public SaResult updateUserInfo(
      @Validated(UpdateUserInfoGroup.class) CustomerUserDto customerUserDto) {

    customerUserDto.setLoginId(StpUtil.getLoginId().toString());
    SysUserBo sysUserBo = SysUserBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.updateUserInfo(sysUserBo);
  }

  @PostMapping("sendMailCode")
  public SaResult sendMailCode(@Validated(SendMailGroup.class) CustomerUserDto customerUserDto) {

    return customerUserService.sendMailCode(customerUserDto.getAccount());
  }

  @PostMapping("resetPassword")
  public SaResult resetPassword(
      @Validated(ResetPassWordGroup.class) CustomerUserDto customerUserDto) {

    SysUserBo sysUserBo = SysUserBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.resetPassword(sysUserBo);
  }

  @PostMapping("realName")
  public SaResult realName(@Validated(RealNameGroup.class) CustomerUserDto customerUserDto) {

    customerUserDto.setLoginId(StpUtil.getLoginId().toString());
    SysUserBo sysUserBo = SysUserBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.realName(sysUserBo);
  }

  @PostMapping("loginByEmail")
  public SaResult loginByEmail(
      @Validated(LoginByEmailGroup.class) LoginByEmailDto loginByEmailDto) {
    LoginByEmailBo loginByEmailBo = LoginByEmailBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(loginByEmailDto, loginByEmailBo);
    return customerUserService.loginByEmail(loginByEmailBo);
  }

  @RequestMapping("upload")
  public SaResult uploadFile(@RequestPart("file") MultipartFile file) {
    return customerUserService.uploadFile(file);
  }
}
