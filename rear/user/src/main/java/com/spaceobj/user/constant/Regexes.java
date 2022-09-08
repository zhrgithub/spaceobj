package com.spaceobj.user.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/8/19 12:47
 */
public class Regexes {

  /** 手机号校验：只要是13,14,15,16,17,18,19开头即可 */
  public static final String REGEX_PHONE = "/^(?:(?:\\+|00)86)?1[3-9]\\d{9}$/";

  /** 昵称校验：中文姓名 */
  public static final String NICKNAME = "/^(?:[\\u4e00-\\u9fa5·]{2,16})$/";
}
