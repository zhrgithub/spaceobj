package com.spaceobj.gateway.fallback;

import com.spaceobj.gateway.component.UserClient;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/24 20:15
 */
@Component
public class UserClientFallback implements UserClient {

    @Override
    public byte[] getUserPermissionByAccount(String account) {

        return null;
    }

}
