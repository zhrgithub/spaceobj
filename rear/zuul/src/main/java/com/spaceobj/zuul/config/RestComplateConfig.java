package com.spaceobj.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhr_java@163.com
 * @date 2022/7/17 22:10
 */
@Configuration
public class RestComplateConfig {

    /**
     * 实例化 RestTemplate 实例
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

}
