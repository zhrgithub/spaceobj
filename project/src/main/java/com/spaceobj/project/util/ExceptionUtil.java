package com.spaceobj.project.util;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

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

    // 堆栈信息
    StackTraceElement ste = e.getStackTrace()[0];
    // 类名
    String className = ste.getClassName();
    // 方法名
    String methodName = ste.getMethodName();
    // 行号
    Integer lineNumber = ste.getLineNumber();
    // 文件名
    String fileName = ste.getFileName();
    // 创建时间
    Date createTime = new Date();

    // 异常内容
    String content = sw.toString();

    log.error("===========start============");
    log.error("创建时间:" + createTime);
    log.error("文件名:" + fileName);
    log.error("类名:" + className);
    log.error("方法名:" + methodName);
    log.error("行号:" + lineNumber);
    log.error("异常内容:" + content);
    log.error("============end=============");
  }
}
