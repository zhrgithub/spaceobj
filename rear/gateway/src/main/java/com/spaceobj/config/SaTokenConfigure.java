package com.spaceobj.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * [Sa-Token 权限认证] 配置类
 *
 * @author zhr_java@163.com
 * @date 2022/9/11 17:35
 */
@Configuration
public class SaTokenConfigure {

  /**
   * 注册 Sa-Token全局过滤器
   *
   * @return
   */
  @Bean
  public SaReactorFilter getSaReactorFilter() {
    return new SaReactorFilter()
        // 拦截地址
        .addInclude("/**")
        // 开放地址
        .addExclude("/favicon.ico")
        .addExclude("/spaceobj-other/other/getOther")
        .addExclude("/spaceobj-user/customerUser/sendMailCode")
        .addExclude("/spaceobj-user/customerUser/resetPassword")
        .addExclude("/spaceobj-user/customerUser/loginByWechat")
        .addExclude("/spaceobj-user/customerUser/bindWechat")
        .addExclude("/spaceobj-user/sysUser/getUserInfoByAccount")
        .addExclude("/spaceobj-user/sysUser/getUserPermissionByAccount")
        .addExclude("/spaceobj-user/sysUser/getSysUserByUserId")
        .addExclude("/spaceobj-advertise/jd/list")
        .addExclude("/spaceobj-project/project/findList")
        .addExclude("/spaceobj-project/project/addPageViews")
        .addExclude("/spaceobj-project/project/getEncryptProjectByUUID")
        .addExclude("/spaceobj-project-help/projectHelp/getProjectHelpLink")
        // 鉴权方法：每次访问进入
        .setAuth(
            obj -> {
              // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
              SaRouter.match(
                  "/**", "/spaceobj-user/customerUser/loginOrRegister", r -> StpUtil.checkLogin());

              // 权限认证 -- 不同模块, 校验不同权限
              SaRouter.match(
                  "/spaceobj-other/other/updateOther", r -> StpUtil.checkPermission("other"));
              SaRouter.match(
                  "/spaceobj-user/sysUser/findList", r -> StpUtil.checkPermission("user"));
              SaRouter.match(
                  "/spaceobj-user/sysUser/updateUser", r -> StpUtil.checkPermission("user"));
              SaRouter.match(
                  "/spaceobj-user/sysPhoto/addOrUpdate", r -> StpUtil.checkPermission("user"));
              SaRouter.match(
                  "/spaceobj-user/sysPhoto/delete", r -> StpUtil.checkPermission("user"));

              SaRouter.match("/spaceobj-advertise/**", r -> StpUtil.checkPermission("advertise"));
              SaRouter.match(
                  "/spaceobj-project/project/auditProject",
                  r -> StpUtil.checkPermission("project"));
              SaRouter.match(
                  "/spaceobj-project/project/queryListAdmin",
                  r -> StpUtil.checkPermission("project"));
            })
        // 异常处理方法：每次setAuth函数出现异常时进入
        .setError(
            e -> {
              if (e instanceof NotLoginException) {
                return SaResult.error(e.getMessage()).setCode(201);
              }
              return SaResult.error(e.getMessage());
            });
  }
}
