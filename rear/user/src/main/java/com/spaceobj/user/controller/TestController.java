package com.spaceobj.user.controller;

import com.spaceobj.user.constent.KafkaSender;
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


    @Autowired
    private KafkaSender kafkaSender;


    @GetMapping("sendMessage/{msg}")
    public void sendMessage(@PathVariable("msg") String msg){
        kafkaSender.send(msg);
    }
}