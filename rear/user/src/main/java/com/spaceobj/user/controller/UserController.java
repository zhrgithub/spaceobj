package com.spaceobj.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
import com.spaceobj.user.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/7/24 00:23
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    // 会话登录接口
    @RequestMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        // 第一步：比对前端提交的账号名称、密码
        if("zhang".equals(name) && "123456".equals(pwd)) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(10001);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }



    @RequestMapping("test")
    public SaResult test() {
        System.out.println(StpUtil.isLogin()+","+StpUtil.getLoginId()+","+StpUtil.getPermissionList()+","+StpUtil.getRoleList());

        return SaResult.ok("test成功");
    }

    @RequestMapping("addUser")
    public ResultData addUser(SysUser sysUser){
        return sysUserService.addUser(sysUser);
    }






}
