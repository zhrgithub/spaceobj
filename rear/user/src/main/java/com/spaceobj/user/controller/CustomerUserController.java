package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.bo.LoginOrRegisterBo;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.dto.CustomerUserDto;
import com.spaceobj.user.service.CustomerUserService;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
  public SaResult loginOrRegister(CustomerUserDto customerUserDto) {

    LoginOrRegisterBo loginOrRegisterBo = LoginOrRegisterBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginOrRegisterBo);

    return customerUserService.loginOrRegister(loginOrRegisterBo);
  }

  @PostMapping("loginOut")
  public SaResult loginOut(CustomerUserDto customerUserDto) {

    LoginOrRegisterBo loginOrRegisterBo = LoginOrRegisterBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginOrRegisterBo);

    return customerUserService.loginOut(loginOrRegisterBo.getAccount());
  }

  @PostMapping("getUserInfo")
  public SaResult getUserInfo(CustomerUserDto customerUserDto) {

    LoginOrRegisterBo loginOrRegisterBo = LoginOrRegisterBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, loginOrRegisterBo);

    return customerUserService.getUserInfo(loginOrRegisterBo.getAccount());
  }

  @PostMapping("updateUserInfo")
  public SaResult updateUserInfo(CustomerUserDto customerUserDto) {

    SysUserBo sysUserBo = SysUserBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.updateUserInfo(sysUserBo);
  }

  @PostMapping("sendMailCode")
  public SaResult sendMailCode(CustomerUserDto customerUserDto) {

    return customerUserService.sendMailCode(customerUserDto.getAccount());
  }

  @PostMapping("resetPassword")
  public SaResult resetPassword(CustomerUserDto customerUserDto) {

    SysUserBo sysUserBo = SysUserBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.resetPassword(sysUserBo);
  }

  @PostMapping("realName")
  public SaResult realName(CustomerUserDto customerUserDto) {

    SysUserBo sysUserBo = SysUserBo.builder().build();
    // 将Dto转化成bo
    BeanConvertToTargetUtils.copyNotNullProperties(customerUserDto, sysUserBo);

    return customerUserService.realName(sysUserBo);
  }

  @RequestMapping("upload")
  public SaResult uploadFile(@RequestPart("file") MultipartFile file) {
    return customerUserService.uploadFile(file);
  }
}
