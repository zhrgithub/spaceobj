package com.spaceobj.user.aop;

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
                  // 根据路由划分模块，不同模块不同鉴权
                  SaRouter.match("/user/test", r -> StpUtil.checkPermission("admin"));
                }))
        .addPathPatterns("/**")
        .excludePathPatterns("/user/doLogin");
  }
}
