package com.spaceobj.user.utils;

import org.springframework.util.Base64Utils;

/**
 * @author zhr_java@163.com
 * @date 2022/9/18 11:30
 */
public class Base64 {

  /**
   * base64解码，将编码后字符串类型的base64转化为解码后的字符串
   *
   * @param str
   * @return
   */
  public static String decode(String str) {
    String decodeStr = null;
    try {
      str = str.replaceAll(" +", "+");
      byte[] bytes = str.getBytes();
      decodeStr = bytesToHexString(Base64Utils.decode(bytes));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return decodeStr;
  }

  /**
   * 字节转字符串
   *
   * @param bytes
   * @return
   */
  private static String bytesToHexString(byte[] bytes) {

    StringBuffer sb = new StringBuffer(bytes.length);
    String temp = null;
    for (int i = 0; i < bytes.length; i++) {
      temp = Integer.toHexString(0xFF & bytes[i]);
      if (temp.length() < 2) {
        sb.append(0);
      }
      sb.append(temp);
    }
    return sb.toString();
  }
}
