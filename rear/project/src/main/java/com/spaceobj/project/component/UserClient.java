package com.spaceobj.project.component;

import cn.dev33.satoken.util.SaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:27
 */
@FeignClient(value = "spaceobj-user")
@Component
public interface UserClient {

  @PostMapping(value = "/sysUser/getUserInfoByAccount")
  public SaResult getUserInfoByAccount(@PathVariable("account") String account);

  /**
   * 根据用户id返回实体信息
   *
   * @param userId
   * @return
   */
  @PostMapping(value = "/sysUser/getSysUserByUserId")
  public SaResult getSysUserByUserId(String userId);
}
