package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.constent.KafKaTopics;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.kafka.KafkaSender;
import com.spaceobj.user.utils.ReceiveEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:30
 */
@RestController
@Slf4j
public class TestController {

  @Autowired private KafkaSender kafkaSender;

  @GetMapping("test2")
  public SaResult test2() {

    return SaResult.ok().setData("12121");
  }





  @GetMapping("sendMessage")
  public void sendMessage() {
    ReceiveEmail receiveEmail =
            ReceiveEmail.builder()
                    .receiverEmail("2923038658@qq.com")
                    .title("spaceobj")
                    .content("邮箱验证码：323643")
                    .build();

    kafkaSender.send(receiveEmail, KafKaTopics.EMAIL_VERIFICATION_CODE);
  }

  @PostMapping("testConvertTarget")
  public void testConvertTargert(){

    SysUser sysUser = SysUser.builder().username("测试").password("hahaha").deviceType("iphone13 pro 5T").build();
    kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
  }




}
