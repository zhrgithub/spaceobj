package com.spaceobj.projectHelp.aop;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhr_java@163.com
 * @date 2022/7/24 00:46
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

  /**
   * 注册拦截器
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    // 注册 Sa-Token 的路由拦截器
    registry
        .addInterceptor(
            new SaRouteInterceptor(
                (req, res, handler) -> {

                  /** 拦截所有接口 */
                  SaRouter.match("/**", r -> StpUtil.checkLogin());

                  /** 二次拦截，权限认证 -- 不同模块认证不同权限 */
                  SaRouter.match("/user/test", r -> StpUtil.checkPermission("admin"));
                }))
        .addPathPatterns("/**")

        /** 放开拦截的接口 */
        .excludePathPatterns(new String[] {"/user/doLogin", "/test2"});
  }
}
