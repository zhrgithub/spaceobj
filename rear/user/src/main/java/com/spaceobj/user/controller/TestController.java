package com.spaceobj.user.controller;

import com.spaceobj.user.constent.KafKaTopics;
import com.spaceobj.user.service.kafka.KafkaSender;
import com.spaceobj.user.utils.ReceiveEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:30
 */
@RestController
@Slf4j
public class TestController {

  @Autowired private KafkaSender kafkaSender;

  @GetMapping("sendMessage/{msg}")
  public void sendMessage(@PathVariable("msg") String msg) {

    ReceiveEmail receiveEmail =
        ReceiveEmail.builder()
            .receiverEmail("2923038658@qq.com")
            .title("spaceobj")
            .content("邮箱验证码：323643")
            .build();

    kafkaSender.send(receiveEmail.getGson(), KafKaTopics.EMAIL_VERIFICATION_CODE);
  }
}
