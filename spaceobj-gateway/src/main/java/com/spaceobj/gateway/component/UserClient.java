package com.spaceobj.gateway.component;

import com.spaceobj.gateway.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:27
 */
@Component
@FeignClient(contextId = "spaceobj-gateway",
        name = "spaceobjGateway",
        fallback = UserClientFallback.class)
public interface UserClient {

    /**
     * 根据账户获取用户权限
     *
     * @param account
     *
     * @return
     */
    @PostMapping(value = "/spaceobj-user/sysUser/getUserPermissionByAccount")
    public byte[] getUserPermissionByAccount(
            @RequestParam("account")
            String account);

}
