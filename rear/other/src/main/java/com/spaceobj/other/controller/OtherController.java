package com.spaceobj.other.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.domain.Other;
import com.spaceobj.other.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public SaResult getOther(){
        return otherService.getOther();
    }


    @PostMapping("updateOther")
    public SaResult updateOther(@Validated Other other){

        return otherService.updateOther(other);
    }

}
