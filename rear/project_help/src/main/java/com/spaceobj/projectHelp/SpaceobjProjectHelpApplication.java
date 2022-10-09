package com.spaceobj.projectHelp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
