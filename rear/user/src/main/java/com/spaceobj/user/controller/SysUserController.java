package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.dto.SysUserDto;
import com.spaceobj.user.group.sysUser.FindListSysUserGroup;
import com.spaceobj.user.group.sysUser.UpdateSysUserGroup;
import com.spaceobj.user.service.SysUserService;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/9/4 17:15
 */
@RestController
@RequestMapping(value = "/sysUser", method = RequestMethod.POST)
public class SysUserController {

  @Autowired private SysUserService sysUserService;

  @PostMapping("findList")
  public SaResult findList(@Validated(FindListSysUserGroup.class) SysUserDto sysUserDto) {
    SysUserBo sysUserBo = SysUserBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(sysUserDto, sysUserBo);
    return sysUserService.findList(sysUserBo);
  }

  @PostMapping("updateUser")
  public SaResult updateUser(@Validated(UpdateSysUserGroup.class) SysUserDto sysUserDto) {
    System.out.println("sysUser: " + sysUserDto.toString());
    SysUserBo sysUserBo = SysUserBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(sysUserDto, sysUserBo);
    return sysUserService.updateSysUser(sysUserBo);
  }

  @PostMapping(value = "getUserInfoByAccount")
  public byte[] getUserInfoByAccount(String account) {
    return sysUserService.getUserInfoByAccount(account);
  }

  @PostMapping(value = "getSysUserByUserId")
  public SaResult getSysUserByUserId(String userId) {
    return sysUserService.getSysUserByUserId(userId);
  }
}
