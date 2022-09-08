package com.spaceobj.project.constant;

import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 22:05
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public SaResult exceptionHandler(BindException exception) {
    BindingResult result = exception.getBindingResult();
    StringBuilder stringBuilder = new StringBuilder();
    if (result.hasErrors()) {
      List<ObjectError> errors = result.getAllErrors();
      if (errors != null) {
        errors.forEach(
            p -> {
              FieldError fieldError = (FieldError) p;
              log.warn(
                  "Bad Request Parameters: dto entity [{}],field [{}],message [{}]",
                  fieldError.getObjectName(),
                  fieldError.getField(),
                  fieldError.getDefaultMessage());
              stringBuilder.append(fieldError.getDefaultMessage());
            });
      }
    }
    return SaResult.error(stringBuilder.toString());
  }

  /**
   * 404异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public SaResult handlerNoFoundException(Exception e) {
    return SaResult.error("路径不存在");
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
    return SaResult.error("系统繁忙,请稍后再试");
  }
}
