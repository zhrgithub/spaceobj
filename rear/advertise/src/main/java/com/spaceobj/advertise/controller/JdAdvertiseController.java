package com.spaceobj.advertise.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.advertise.service.JdAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/7/17 22:50
 */
@CrossOrigin
@RestController
@RequestMapping("jd")
public class JdAdvertiseController {

    @Autowired
    private JdAdvertiseService jdAdvertiseService;

    @GetMapping(value = "list")
    public SaResult list(){
        return jdAdvertiseService.findList();
    }
}
