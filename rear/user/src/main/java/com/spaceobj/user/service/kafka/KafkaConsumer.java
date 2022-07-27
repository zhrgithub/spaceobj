package com.spaceobj.user.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:30
 */
@Component
@Slf4j
public class KafkaConsumer {

  @KafkaListener(topics = {"hello2"})
  public void listen(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              log.info("【+++++++++++++++++ record = {} 】", record);
              log.info("【+++++++++++++++++ message = {}】", message);
            });
  }
}
