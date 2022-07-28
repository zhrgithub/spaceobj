package com.spaceobj.email.constent;

import cn.dev33.satoken.util.SaResult;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 22:05
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * kafka通信异常
   *
   * @param ex
   */
    @ExceptionHandler(KafkaException.class)
    @ResponseBody
    public void notLoginException(KafkaException ex) {
      log.error("kafka通信异常");
    }

  /**
   * 空指针校验
   *
   * @param ex
   */
  @ExceptionHandler(NullPointerException.class)
  @ResponseBody
  public void nullPointerException(NullPointerException ex) {

    log.error("空指针异常");
  }

  /**
   * 全局异常拦截
   *
   * @param e
   * @return
   */
  @ExceptionHandler
  public SaResult handlerException(Exception e) {
    e.printStackTrace();
    return SaResult.error(e.getMessage());
  }
}
