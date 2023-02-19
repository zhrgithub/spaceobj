package com.spaceobj.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
@EnableCircuitBreaker
public class SpaceobjGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpaceobjGatewayApplication.class, args);
    }

}
