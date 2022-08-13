package com.spaceobj.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhr_java@163.com
 * @date 2022/7/24 00:23
 */
@RestController
@RequestMapping("user")
public class UserController {
  @Autowired private SysUserService sysUserService;


  @PostMapping("doLogin")
  public SaResult doLogin(String phoneNumber) {

    StpUtil.login(phoneNumber);
    return SaResult.ok("登录成功");
    // 第一步：比对前端提交的账号名称、密码
//    if ("13362620045".equals(phoneNumber) && "zhr_java@163.com".equals(email)&&"123456".equals(password)) {
//      // 第二步：根据账号id，进行登录
//      StpUtil.login(email);
//      return SaResult.ok("登录成功");
//    }
//    return SaResult.error("登录失败");
  }

  @RequestMapping("test")
  public SaResult test() {
    System.out.println(
        StpUtil.isLogin()
            + ","
            + StpUtil.getLoginId()
            + ","
            + StpUtil.getPermissionList()
            + ","
            + StpUtil.getRoleList());

    return SaResult.ok("test成功");
  }





}
