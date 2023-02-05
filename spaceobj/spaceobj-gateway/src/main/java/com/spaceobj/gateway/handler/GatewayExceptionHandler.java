package com.spaceobj.gateway.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;

/**
 * 网关统一异常处理
 *
 * @author zhr_java@163.com
 * @date 2022/9/12 21:32
 */
@Order(-1)
@Configuration
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    ServerHttpResponse response = exchange.getResponse();
    if (exchange.getResponse().isCommitted()) {
      return Mono.error(ex);
    }
    String msg;
    if (ex instanceof NotFoundException) {
      msg = "服务未找到";
    } else if (ex instanceof ResponseStatusException) {
      ResponseStatusException responseStatusException = (ResponseStatusException) ex;
      msg = responseStatusException.getMessage();
    } else if(ex instanceof PrematureCloseException){
      msg = "图片不得超过200kb";
      ex.printStackTrace();
    }else{
      msg = "内部服务器错误";
      ex.printStackTrace();
    }

    log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getPath(), ex.getMessage());

    return responseWrap(response,msg);
  }

  /**
   * 返回错误消息体
   *
   * @param response
   * @return
   */
  private static Mono<Void> responseWrap(ServerHttpResponse response,String msg) {
    response.setStatusCode(HttpStatus.BAD_REQUEST);
    // String data = msg;
    SaResult saResult =  SaResult.error(msg);
    DataBuffer wrap = response.bufferFactory().wrap(saResult.toString().getBytes());
    return response.writeWith(Mono.just(wrap));
  }

  @ExceptionHandler(RetryableException.class)
  public SaResult handlerException(RetryableException e) {
    e.printStackTrace();
    return SaResult.error("feign重试错误");
  }

  @ExceptionHandler(SaTokenException.class)
  public SaResult handlerException(SaTokenException e) {
    e.printStackTrace();
    return SaResult.error("未登录").setCode(201);
  }

  @ExceptionHandler(NotLoginException.class)
  public SaResult handlerException(NotLoginException e) {
    e.printStackTrace();
    return SaResult.error("未登录").setCode(201);
  }


}
