package com.spaceobj.projectHelp.component;

import cn.dev33.satoken.util.SaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:25
 */

@FeignClient(value = "spaceobj-project")
@Component
public interface ProjectClient {

    @PostMapping(value = "/project/getEncryptProjectByUUID")
    public SaResult getEncryptProjectByUUID(String uuid);
}
