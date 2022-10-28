package com.spaceobj.projectHelp.fallback;

import com.spaceobj.projectHelp.component.UserClient;
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
