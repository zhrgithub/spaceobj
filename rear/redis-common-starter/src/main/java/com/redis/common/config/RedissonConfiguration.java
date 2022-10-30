package com.redis.common.config;

import com.redis.common.service.RedissonService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhr_java@163.com
 * @date 2022/10/6 18:24
 */
@Configuration
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
    config.useSingleServer().setAddress("redis://"+redisHost+":"+port).setPassword(password);

    return Redisson.create(config);
  }
}
