package com.spaceobj.user.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/10/10 00:16
 */
public class WechatConstant {

  /** 小程序唯一标识 (在微信小程序管理后台获取) */
  public static final String W_X_S_P_APPID = "wx8205f68699884404";

  /** 小程序的 app secret (在微信小程序管理后台获取) */
  public static final String W_X_S_P_SECRET = "4ce53637b0265e8cc9f1bb0445f28036";

  /** 授权（必填） */
  public static final String GRANT_TYPE = "authorization_code";

  public static final String OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session";
}
