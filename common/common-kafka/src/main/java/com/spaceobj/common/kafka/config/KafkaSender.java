package com.spaceobj.common.kafka.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spaceobj.common.kafka.dto.KafkaMessage;
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

    private Gson gsonBuilder = new GsonBuilder().disableHtmlEscaping().create();

    private Gson gson = new Gson();

    public void send(Object obj, String topicName) {

        KafkaMessage message = new KafkaMessage();

        message.setId(System.currentTimeMillis());

        String msg = gson.toJson(obj, obj.getClass());
        message.setMsg(msg);
        message.setSendTime(new Date());
        kafkaTemplate.send(topicName, gsonBuilder.toJson(message));
    }

}
