package com.spaceobj.user.service.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spaceobj.user.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:28
 */
@Component
@Slf4j
public class KafkaSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  /**
   * 构造器方式注入 kafkaTemplate
   *
   * @param kafkaTemplate
   */
  public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  public void send(String msg,String topicName) {
    Message message = new Message();

    message.setId(System.currentTimeMillis());
    message.setMsg(msg);
    message.setSendTime(new Date());
    kafkaTemplate.send(topicName, gson.toJson(message));
  }
}
