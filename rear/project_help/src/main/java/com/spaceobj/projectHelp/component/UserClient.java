package com.spaceobj.projectHelp.component;

import com.spaceobj.projectHelp.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:27
 */
@Component
@FeignClient(
    contextId = "spaceobj-user",
    name = "spaceobjGateway",
    fallback = UserClientFallback.class)
public interface UserClient {

  /**
   * 根据账户返回用户实体信息
   *
   * @param account
   * @return
   */
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
