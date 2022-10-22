package com.spaceobj.component;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.pojo.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:27
 */
@Component
@FeignClient(contextId = "spaceobj-user",name = "spaceobjGateway")
public interface UserClient {

  @PostMapping(value = "/spaceobj-user/sysUser/getUserInfoByAccount")
  public byte[] getUserInfoByAccount(@RequestParam("account") String account);
}
