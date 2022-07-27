package com.spaceobj.email.util;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 11:32
 */
@Data
public class Email {

  /** 发件人邮箱 */
  private String sendEmail;

  /** 发件人邮箱@前面的名称 */
  private String sendEmailName;

  /** 发件人邮箱秘钥,邮箱要开启（POP3/SMTP服务） */
  private String password;

  /** 收件人邮箱 */
  private String receiverEmail;

  /** 标题 */
  private String title;

  /** 内容 */
  private String content;
}
