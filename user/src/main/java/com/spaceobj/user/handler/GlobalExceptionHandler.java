package com.spaceobj.user.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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

  /** 请求参数为空 */
  @ExceptionHandler(NullPointerException.class)
  @ResponseBody
  public SaResult nullPointerException(NullPointerException ex) {
    ex.printStackTrace();
    log.error("null point exception info:" + ex.getMessage());
    return SaResult.error("请求参数为空");
  }

  /**
   * 参数类型转换异常捕获
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(NumberFormatException.class)
  @ResponseBody
  public SaResult numberFormatException(NumberFormatException ex) {
    log.error("number format exception info:" + ex.getMessage());
    return SaResult.error("参数类型转换错误");
  }

  /**
   * 非空校验
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(SaTokenException.class)
  @ResponseBody
  public SaResult saTokenException(SaTokenException ex) {
    log.error("sa token exception info:" + ex.getMessage());
    return SaResult.error(ex.getMessage());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  public SaResult httpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    log.error("httpRequest method notSupported exception info:" + ex.getMessage());
    return SaResult.error("请求方法不支持");
  }

  /**
   * 登录校验
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(NotLoginException.class)
  @ResponseBody
  public SaResult notLoginException(NotLoginException ex) {
    ex.printStackTrace();
    log.error("not login exception:" + ex.getMessage());
    return SaResult.error("登录后操作");
  }

  /**
   * 权限校验
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(NotPermissionException.class)
  @ResponseBody
  public SaResult notPermissionException(NotPermissionException ex) {
    return SaResult.error("无此权限");
  }

  /**
   * 文件大小校验
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseBody
  public SaResult processException(MaxUploadSizeExceededException ex) {
    return SaResult.error("文件不得超过200kb");
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
