package com.spaceobj.user.constent;

/**
 * @author zhr_java@163.com
 * @date 2022/8/19 12:47
 */
public class Regexes {

  /** 邮箱地址 */
  public static final String REGEX_EMAIL =
      "/^(([^<>()[\\]\\\\.,;:\\s@\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/";

  /** 密码校验：最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符 */
  public static final String REGEX_PASSWORD =
      "/^\\S*(?=\\S{6,})(?=\\S*\\d)(?=\\S*[A-Z])(?=\\S*[a-z])(?=\\S*[!@#$%^&*? ])\\S*$/";

  /** 手机号校验：只要是13,14,15,16,17,18,19开头即可 */
  public static final String REGEX_PHONE = "/^(?:(?:\\+|00)86)?1[3-9]\\d{9}$/";

  /** 昵称校验：中文姓名 */
  public static final String NICKNAME = "/^(?:[\\u4e00-\\u9fa5·]{2,16})$/";
}
