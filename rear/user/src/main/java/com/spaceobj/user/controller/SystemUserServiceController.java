package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 20:53
 */
@RestController("systemUserServiceController")
@RequestMapping("/systemUserService")
public class SystemUserServiceController {

  @Autowired private SysUserService sysUserService;

  @PostMapping(value = "getUserInfoByAccount")
  public SaResult getUserInfoByAccount(@PathVariable("account") String account) {
    return sysUserService.getUserInfoByAccount(account);
  }
}
