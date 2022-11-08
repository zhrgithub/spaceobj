package com.spaceobj.project.component;

import com.spaceobj.project.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:27
 */
@Component
@FeignClient(contextId = "spaceobj-project", name = "spaceobjGateway",fallback = UserClientFallback.class)
public interface UserClient {

  @PostMapping(value = "/spaceobj-user/sysUser/getUserInfoByAccount")
  public byte[] getUserInfoByAccount(@RequestParam("account") String account);

  /**
   * 根据用户id返回实体信息
   *
   * @param userId
   * @return
   */
  @PostMapping(value = "/spaceobj-user/sysUser/getSysUserByUserId")
  public byte[] getSysUserByUserId(@RequestParam("userId") String userId);
}
