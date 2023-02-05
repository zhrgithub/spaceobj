package com.spaceobj.common.core.utils;

import java.security.SecureRandom;

/**
 * @author zhr_java@163.com
 * @date 2022/8/18 19:41
 */
public class EmailVerifyCode {

  public static final String getVerifyCode() {
    String verifyCode = "";
    SecureRandom securityRandom = new SecureRandom();
    for (int i = 0; i < 6; i++) {
      verifyCode = verifyCode + securityRandom.nextInt(10);
    }

    return verifyCode;
  }
}
