package com.spaceobj.project.fallback;

import com.spaceobj.project.component.ProjectHelpClient;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/24 20:15
 */
@Component
public class ProjectHelpClientFallback implements ProjectHelpClient {
    @Override
    public byte[] getProjectHelpLink(String pUUID, String userId) {
        return null;
    }

}
