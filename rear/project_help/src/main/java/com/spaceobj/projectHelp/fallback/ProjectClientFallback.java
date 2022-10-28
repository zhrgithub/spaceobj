package com.spaceobj.projectHelp.fallback;

import com.spaceobj.projectHelp.component.ProjectClient;
import com.spaceobj.projectHelp.component.UserClient;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/24 20:15
 */
@Component
public class ProjectClientFallback implements ProjectClient {

    @Override
    public byte[] getEncryptProjectByUUID(String uuid) {

        return null;
    }

}
