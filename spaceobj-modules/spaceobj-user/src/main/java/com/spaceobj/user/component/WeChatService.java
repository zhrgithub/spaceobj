package com.spaceobj.user.component;

import cn.hutool.json.JSONObject;
import com.spaceobj.common.core.utils.HttpRequest;
import com.spaceobj.user.constant.WechatConstant;
import org.springframework.stereotype.Component;

/**
 * @author zhr
 */
@Component
public class WeChatService {

  /**
   * 获取用户的openId
   *
   * @param code
   * @return
   */
  public String getOpenIdByCode(String code) {
    // 登录凭证不能为空
    if (code == null || code.length() == 0) {
      return null;
    }
    // 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
    String params =
        "appid="
            + WechatConstant.W_X_S_P_APPID
            + "&secret="
            + WechatConstant.W_X_S_P_SECRET
            + "&js_code="
            + code
            + "&grant_type="
            + WechatConstant.GRANT_TYPE;
    // 发送请求
    String sr = HttpRequest.sendPost(WechatConstant.OPENID_URL, params);
    // 解析相应内容（转换成json对象）
    JSONObject json = new JSONObject(sr);
    System.out.println(json);
    String openid = (String) json.get("openid");
    return openid;
  }
}
