package com.spaceobj.user.utils;

import com.alibaba.fastjson.JSON;
import com.spaceobj.user.constant.ExceptionMessage;
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

    ExceptionMessage exceptionMessage =
        ExceptionMessage.builder()
            .className(ste.getClassName())
            .methodName(ste.getMethodName())
            .lineNumber(ste.getLineNumber())
            .fileName(ste.getFileName())
            .createTime(new Date())
            .content(sw.toString())
            .build();
    log.error(
        "===start=====>\n"
            + "---exceptionMessage: \n"
            + "---className: " + exceptionMessage.getClassName() + "\n"
            + "---methodName: " + exceptionMessage.getMethodName() + "\n"
            + "---linNumber: " + exceptionMessage.getLineNumber() + "\n"
            + "---fileName: " + exceptionMessage.getFileName() + "\n"
            + "---createTime: " + exceptionMessage.getCreateTime() + "\n"
            + "---content: " + exceptionMessage.getContent() + "\n"
            + "===========end==>");
  }

  public static void main(String[] args) {
    try {
      int[] arr = {1, 3, 2, 4};
      System.out.println(arr[6]);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
    }
  }
}
