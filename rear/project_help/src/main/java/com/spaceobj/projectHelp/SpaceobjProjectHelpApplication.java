package com.spaceobj.projectHelp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.CircuitBreaker;

/**
 * @author zhr
 */
@EnableFeignClients        // 开启Feign客户端
@SpringBootApplication
public class SpaceobjProjectHelpApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpaceobjProjectHelpApplication.class, args);
    }

}
