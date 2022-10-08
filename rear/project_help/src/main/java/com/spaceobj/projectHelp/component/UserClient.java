package com.spaceobj.projectHelp.component;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:27
 */
@FeignClient(value = "spaceobj-user")
@Component
public interface UserClient {

}
