package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.pojo.Other;
import com.spaceobj.user.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:31
 */
@RestController
@RequestMapping("other")
public class OtherController {

    @Autowired
    private OtherService otherService;

    @PostMapping("getOther")
    public SaResult getOther() {

        return otherService.getOther();
    }

    @PostMapping("updateOther")
    public SaResult updateOther(
            @Validated
            Other other) {

        return otherService.updateOther(other);
    }

}
