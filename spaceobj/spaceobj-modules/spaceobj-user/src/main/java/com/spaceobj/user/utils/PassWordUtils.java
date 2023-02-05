package com.spaceobj.user.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.spaceobj.common.core.utils.Base64Util;
import com.spaceobj.common.core.utils.ExceptionUtil;

/**
 * @author zhr_java@163.com
 * @date 2022/9/18 12:10
 */
public class PassWordUtils {

  /**
   * 密码转MD5
   *
   * @param privateKey 秘钥
   * @param password base64加密后的密码
   * @return md5加密后的密码
   */
  public static String passwordToMD5(String privateKey, String password) {
    // md5加密
    String md5Password = null;
    try {

      // 秘钥解密
      String rsaDecryptPassword =
          SaSecureUtil.rsaDecryptByPrivate(privateKey, Base64Util.decode(password));
      md5Password = SaSecureUtil.md5(rsaDecryptPassword);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return null;
    }

    return md5Password;
  }
}
