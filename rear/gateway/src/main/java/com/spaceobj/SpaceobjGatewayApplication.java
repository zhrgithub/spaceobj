package com.spaceobj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhr
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class SpaceobjGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpaceobjGatewayApplication.class, args);
    }

}
