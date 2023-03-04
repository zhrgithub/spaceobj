package com.spaceobj.common.redis.config;

import com.spaceobj.common.redis.service.RedissonService;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhr_java@163.com
 * @date 2022/10/6 18:24
 */
@Configuration
@RefreshScope
public class RedissonConfiguration {

    @Bean
    public RedissonService redissonService() {

        return new RedissonService();
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient getRedisson() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisHost + ":" + port);
        if (!StringUtils.isEmpty(password)) {
            config.useSingleServer().setPassword(password);
        }
        return Redisson.create(config);
    }

}
