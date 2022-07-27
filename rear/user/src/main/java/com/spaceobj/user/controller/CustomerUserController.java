package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.service.CustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 19:41
 */
@RestController
@RequestMapping("customerUser")
public class CustomerUserController {

    @Autowired
    private CustomerUserService customerUserService;

    @RequestMapping("upload")
    public SaResult uploadFile(@RequestPart("file") MultipartFile file){
        return customerUserService.uploadFile(file);
    }
}
