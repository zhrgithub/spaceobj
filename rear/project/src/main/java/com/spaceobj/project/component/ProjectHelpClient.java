package com.spaceobj.project.component;

import com.spaceobj.project.fallback.ProjectHelpClientFallback;
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
    contextId = "spaceobj-project-help",
    name = "spaceobjGateway",
    fallback = ProjectHelpClientFallback.class)
public interface ProjectHelpClient {

  /**
   * 根据项目id和UUID获取项目助力链接
   *
   * @param pUUID
   * @param userId
   * @return
   */
  @PostMapping(value = "/spaceobj-project-help/projectHelp/getProjectHelpLink")
  public byte[] getProjectHelpLink(
      @RequestParam("pUUID") String pUUID, @RequestParam("userId") String userId);
}
