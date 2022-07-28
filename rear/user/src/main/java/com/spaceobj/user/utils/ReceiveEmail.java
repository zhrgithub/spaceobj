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

}
