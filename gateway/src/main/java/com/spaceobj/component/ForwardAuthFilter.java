package com.spaceobj.component;

import cn.dev33.satoken.id.SaIdUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * 全局过滤器，为请求添加 Id-Token
 *
 * @author zhr_java@163.com
 * @date 2022/9/11 17:43
 */
@Component
public class ForwardAuthFilter implements GlobalFilter, Ordered {
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest newRequest =
        exchange
            .getRequest()
            .mutate()
            // 为请求追加 Id-Token 参数
            .header(SaIdUtil.ID_TOKEN, SaIdUtil.getToken())
            .build();
    ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
    return chain.filter(newExchange);
  }

  @Override
  public int getOrder() {

    return 1;
  }
}
