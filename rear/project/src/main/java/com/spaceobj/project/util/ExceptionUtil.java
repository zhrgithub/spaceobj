package com.spaceobj.project.util;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author zhr_java@163.com
 * @date 2022/11/1 00:01
 */
@Slf4j
public class ExceptionUtil {

  /**
   * 打印异常日志
   *
   * @param e
   */
  public static void exceptionToString(Exception e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw, true));
    String str = sw.toString();
    log.error("=========start==========");
    log.error(str);
    log.error("=========end=============");



  }
}
