package com.spaceobj.advertise;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.spaceobj.advertise.mapper")
public class SpaceobjAdvertiseApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpaceobjAdvertiseApplication.class, args);
    }

}
