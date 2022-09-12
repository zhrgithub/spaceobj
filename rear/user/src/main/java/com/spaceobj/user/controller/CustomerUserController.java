package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.bo.LoginOrRegisterBo;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.dto.CustomerUserDto;
import com.spaceobj.user.group.customer.*;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginOrRegisterBo);

    return customerUserService.loginOrRegister(loginOrRegisterBo);
  }

  @PostMapping("loginOut")
  public SaResult loginOut(@Validated(LoginOutGroup.class) CustomerUserDto customerUserDto) {
    return customerUserService.loginOut(customerUserDto.getAccount());
  }

  @PostMapping("getUserInfo")
  public SaResult getUserInfo(@Validated(GetUserInfoGroup.class) CustomerUserDto customerUserDto) {

    return customerUserService.getUserInfo(customerUserDto.getAccount());
  }

  @PostMapping("updateUserInfo")
  public SaResult updateUserInfo(
      @Validated(UpdateUserInfoGroup.class) CustomerUserDto customerUserDto) {

    SysUserBo sysUserBo = SysUserBo.builder().build();
    // 将Dto转化成bo
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
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.resetPassword(sysUserBo);
  }

  @PostMapping("realName")
  public SaResult realName(@Validated(RealNameGroup.class) CustomerUserDto customerUserDto) {

    SysUserBo sysUserBo = SysUserBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.realName(sysUserBo);
  }

  @RequestMapping("upload")
  public SaResult uploadFile(@RequestPart("file") MultipartFile file) {
    return customerUserService.uploadFile(file);
  }

  @PostMapping("aesEncrypt")
  public SaResult aesEncrypt(String text) {
    return customerUserService.aesEncrypt(text);
  }
}
