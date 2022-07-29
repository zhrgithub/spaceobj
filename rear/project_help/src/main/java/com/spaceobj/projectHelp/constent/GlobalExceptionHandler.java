package com.spaceobj.projectHelp.constent;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.util.SaResult;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 22:05
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

  /**
   * 登录校验
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(NotLoginException.class)
  @ResponseBody
  public SaResult notLoginException(NotLoginException ex) {
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
  public SaResult notLoginException(NotPermissionException ex) {
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
