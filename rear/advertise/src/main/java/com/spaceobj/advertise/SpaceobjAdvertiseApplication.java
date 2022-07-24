package com.spaceobj.advertise;

import cn.dev33.satoken.SaManager;
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

        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }

}
