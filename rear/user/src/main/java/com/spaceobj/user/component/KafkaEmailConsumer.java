package com.spaceobj.user.component;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spaceobj.user.constant.KafKaTopics;
import com.spaceobj.user.pojo.Email;
import com.spaceobj.user.pojo.SysEmail;
import com.spaceobj.user.service.SysEmailService;
import com.spaceobj.user.utils.CommonStringUtils;
import com.spaceobj.user.utils.Message;
import com.spaceobj.user.utils.SendMail;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:30
 */
@Component
@Slf4j
public class KafkaEmailConsumer {

  @Autowired private SysEmailService sysEmailService;

  @KafkaListener(topics = {KafKaTopics.EMAIL_VERIFICATION_CODE})
  public void emailVerificationCode(ConsumerRecord<?, ?> record) {
    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              sendEmail(message);
            });
  }

  @KafkaListener(topics = {KafKaTopics.AUDIT_NOTICE})
  public void auditNotices(ConsumerRecord<?, ?> record) {
    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              sendEmail(message);
            });
  }

  @KafkaListener(topics = {KafKaTopics.AUDIT_RESULT_NOTICE})
  public void auditResultNotices(ConsumerRecord<?, ?> record) {
    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              sendEmail(message);
            });
  }

  @KafkaListener(topics = {KafKaTopics.HELP_PROJECT_SUCCESSFUL})
  public void helpProjectSuccessful(ConsumerRecord<?, ?> record) {
    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              sendEmail(message);
            });
  }

  @KafkaListener(topics = {KafKaTopics.PENDING_REVIEW_PROJECT})
  public void pendingReviewProject(ConsumerRecord<?, ?> record) {
    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              sendEmail(message);
            });
  }

  /**
   * 解析kafka消息从message获取收件人的email，title，content
   *
   * @param message
   * @return
   */
  private Email getEmail(Object message) {
    // 开启复杂处理Map方法
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    // fromJson
    Message messageResult =
        gson.fromJson(message.toString(), new TypeToken<Message>() {}.getType());
    return new Gson().fromJson(messageResult.getMsg(), Email.class);
  }

  /**
   * 获取系统邮箱中的发件人账户和发件人密码
   *
   * @return
   */
  private SysEmail getSysEmail() {
    List<SysEmail> emailList = null;
    Integer num = null;
    try {
      emailList = (List<SysEmail>) sysEmailService.findList().getData();
      SecureRandom random = new SecureRandom();
      num = random.nextInt(emailList.size());
    } catch (Exception e) {
      log.error("空指针异常");
      return new SysEmail();
    }
    return emailList.get(num);
  }

  /**
   * 发送邮件
   *
   * @param message
   */
  private void sendEmail(Object message) {
    SysEmail sysEmail = getSysEmail();
    Email email = getEmail(message);
    if (StringUtils.isBlank(sysEmail.getEmailAccount())
        || StringUtils.isBlank(CommonStringUtils.getEmailAccountName(sysEmail.getEmailAccount()))
        || StringUtils.isBlank(sysEmail.getEmailPassword())) {
      log.error("邮件发送参数错误！停止发送！");
      return;
    }

    email.setSendEmail(sysEmail.getEmailAccount());
    email.setSendEmailName(CommonStringUtils.getEmailAccountName(sysEmail.getEmailAccount()));
    email.setPassword(sysEmail.getEmailPassword());

    if (StringUtils.isBlank(email.getReceiverEmail())
        || StringUtils.isBlank(email.getContent())
        || StringUtils.isBlank(email.getTitle())) {
      log.error("邮件发送参数错误！停止发送！");

    } else {
      SendMail.sendMails(email);
      log.info("发送成功");
    }
  }
}
