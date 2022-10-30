package com.spaceobj.component;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.redis.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import sun.net.util.IPAddressUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zhr_java@163.com
 * @date 2022/9/12 14:07
 */
@Slf4j
@Component
public class BlackListFilter implements GlobalFilter, Ordered {

  @Autowired private RedisService redisService;

  /** 最大请求次数 */
  private static final int MAX_REQUEST_TIME = 3;

  /** 恶意请求次数 */
  private static final int MALICIOUS_REQUESTS = 5;

  /**
   * 过滤器核⼼⽅法
   *
   * @param exchange
   * @param chain
   * @return
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    // 从上下⽂中取出request和response对象
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();
    // 从request对象中获取客户端ip
    String clientIp = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
    // 校验请求是否是正常的ip
    if (!isIpAddressCheck(clientIp)) {
      return responseWrap(response);
    }

    // 校验ip是否在黑名单中,如果是请求ip超过最大请求次数，直接返回错误请求
    if (redisService.hasKey(clientIp)
        && redisService.getCacheObject(clientIp, Integer.class) >= MALICIOUS_REQUESTS) {
      return responseWrap(response);
    }

    // 可以将用户的ip和每秒请求的次数放入Redis中，如果当前用户每秒请求次数超过最大请求次数，返回错误请求
    if (redisService.hasKey(clientIp)) {

      redisService.increment(clientIp);
      // 如果请求次数大于等于攻击次数，1天后解封
      if (redisService.hasKey(clientIp)&&redisService.getCacheObject(clientIp, Integer.class) >= MALICIOUS_REQUESTS) {
        redisService.expire(clientIp, 1, TimeUnit.DAYS);
        return responseWrap(response);
      }
    } else {
      redisService.setCacheObject(clientIp, 1);
      redisService.expire(clientIp, 1, TimeUnit.SECONDS);
    }
    if (redisService.hasKey(clientIp)&&redisService.getCacheObject(clientIp, Integer.class) > MAX_REQUEST_TIME) {
      return responseWrap(response);
    }

    // 合法请求
    return chain.filter(exchange);
  }

  /**
   * 返回值表示当前过滤器的顺序(优先级)，数值越⼩，优先级越⾼
   *
   * @return
   */
  @Override
  public int getOrder() {

    return 0;
  }

  /**
   * 返回错误消息体
   *
   * @param response
   * @return
   */
  private static Mono<Void> responseWrap(ServerHttpResponse response) {
    response.setStatusCode(HttpStatus.BAD_REQUEST);
    String data = "请求频繁";
    DataBuffer wrap = response.bufferFactory().wrap(SaResult.error(data).toString().getBytes());
    return response.writeWith(Mono.just(wrap));
  }

  /**
   * IP正则校验
   *
   * @param address
   * @return
   */
  public static boolean isIpAddressCheck(String address) {

    boolean iPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(address);
    boolean iPv6LiteralAddress = IPAddressUtil.isIPv6LiteralAddress(address);
    // ip有可能是v4,也有可能是v6,滿足任何一种都是合法的ip
    if (!(iPv4LiteralAddress || iPv6LiteralAddress)) {
      return false;
    }
    return true;
  }
}
