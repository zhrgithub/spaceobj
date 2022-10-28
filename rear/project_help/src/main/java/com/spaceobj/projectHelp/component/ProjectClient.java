package com.spaceobj.projectHelp.component;

import com.spaceobj.projectHelp.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:25
 */
@Component
@FeignClient(
    contextId = "spaceobj-project",
    name = "spaceobjGateway",
    fallback = UserClientFallback.class)
public interface ProjectClient {

  /**
   * 根据项目UUID获取项目信息
   *
   * @param uuid
   * @return
   */
  @PostMapping(value = "/spaceobj-project/project/getEncryptProjectByUUID")
  public byte[] getEncryptProjectByUUID(@RequestParam("uuid") String uuid);
}
