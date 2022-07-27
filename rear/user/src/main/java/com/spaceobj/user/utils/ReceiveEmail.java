package com.spaceobj.user.utils;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 11:32
 */
@Data
@Builder
public class ReceiveEmail {

  /** 收件人邮箱 */
  private String receiverEmail;

  /** 标题 */
  private String title;

  /** 内容 */
  private String content;

  public String getGson() {
    // 使用new方法
    Gson gson = new Gson();
    // toJson 将bean对象转换为json字符串
    return gson.toJson(this, ReceiveEmail.class);
  }


}
