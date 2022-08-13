package com.spaceobj.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author zhr
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.spaceobj.user.mapper")
@CrossOrigin
public class SpaceobjUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpaceobjUserApplication.class, args);
    }

}
