package com.spaceobj.user.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.net.util.IPAddressUtil;

/**
 * @author zhr_java@163.com
 * @date 2022/9/12 22:27
 */
public class HttpUtils {

  /**
   * IP正则校验
   *
   * @param address
   * @return
   */
  public static boolean isIpAddressCheck(String address) {

    boolean iPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(address);
    boolean iPv6LiteralAddress = IPAddressUtil.isIPv6LiteralAddress(address);
    // ip有可能是v4,也有可能是v6,滿足任何一种都是合法的ip
    if (!(iPv4LiteralAddress || iPv6LiteralAddress)) {
      return false;
    }
    return true;
  }

  /**
   * 获取用户IP
   *
   * @return
   */
  public static String getIPAddress() {

    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return requestAttributes.getRequest().getRemoteHost();
  }
}
