package com.spaceobj.user.utils;

import com.spaceobj.user.pojo.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 11:32
 */
public class SendMail {
  public static void sendMails(Email email) {
    Logger logger = LoggerFactory.getLogger(SendMail.class);
    logger.info("开始发送邮件！");

    try {
      // 创建邮件对象
      Session session = null;
      Properties props = new Properties();
      // 此处为发送方邮件服务器地址，要根据邮箱的不同需要自行设置
      props.put("mail.smtp.host", "smtp.163.com");
      props.setProperty("mail.transport.protocol", "smtp");
      // SMTP端口号
      props.put("mail.smtp.port", "25");
      // 设置成需要邮件服务器认证
      props.put("mail.smtp.auth", "true");
      props.put("mail.debug", "true");
      session = Session.getInstance(props);
      session.setDebug(true);
      Message message = new MimeMessage(session);
      // 设置发件人
      message.setFrom(new InternetAddress(email.getSendEmail()));
      // 设置收件人
      message.addRecipient(RecipientType.TO, new InternetAddress(email.getReceiverEmail()));
      // 设置标题
      message.setSubject(email.getTitle());
      // 邮件内容，根据自己需要自行制作模板
      message.setContent(email.getContent(), "text/html;charset=utf-8");
      // 发送邮件
      Transport transport = session.getTransport("smtp");
      transport.connect("smtp.163.com", email.getSendEmailName(), email.getPassword());
      // 发送邮件,其中第二个参数是所有已设好的收件人地址
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }
}
