package com.spaceobj.project.fallback;

import com.spaceobj.project.component.UserClient;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/24 20:15
 */
@Component
public class UserClientFallback implements UserClient {

    @Override
    public byte[] getUserInfoByAccount(String account) {

        return null;
    }

    @Override
    public byte[] getSysUserByUserId(String userId) {

        return null;
    }

}
