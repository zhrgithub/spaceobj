package com.spaceobj.common.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zhr_java@163.com
 * @date 2022/11/2 00:35
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessage {

  /** 类名 */
  String className;

  /** 方法名 */
  String methodName;

  /** 行号 */
  Integer lineNumber;

  /** 文件名 */
  String fileName;

  /** 创建时间 */
  Date createTime;

  /** 异常内容 */
  String content;
}
