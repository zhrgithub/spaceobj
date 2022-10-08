package com.spaceobj.projectHelp.component;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/8 14:25
 */

@FeignClient(value = "spaceobj-project")
@Component
public interface ProjectClient {

}


// 标注该类是一个feign接口，value指定生产者的服务（生产者就是实现该接口方法的服务）
// @FeignClient(value = "service-provider")
// @Component
// public interface UserClient {
//     @GetMapping("/user/{id}")
//     User queryById(@PathVariable("id") Long id);
// }
