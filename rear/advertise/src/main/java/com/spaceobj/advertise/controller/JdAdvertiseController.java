package com.spaceobj.advertise.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.advertise.bo.JdAdvertisBo;
import com.spaceobj.advertise.dto.JdAdvertisDto;
import com.spaceobj.advertise.group.DeleteAdvertiseGroup;
import com.spaceobj.advertise.group.InsertAdverTiseGroup;
import com.spaceobj.advertise.group.UpdateAdvertiseGroup;
import com.spaceobj.advertise.service.JdAdvertiseService;
import com.spaceobj.advertise.utils.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhr_java@163.com
 * @date 2022/7/17 22:50
 */
@RestController
@RequestMapping(value = "jd",
        method = RequestMethod.POST)
public class JdAdvertiseController {

    @Autowired
    private JdAdvertiseService jdAdvertiseService;

    @GetMapping(value = "list")
    public SaResult list() {

        return jdAdvertiseService.findList();
    }

    @PostMapping("saveAdvertise")
    public SaResult saveAdvertise(
            @Validated(InsertAdverTiseGroup.class )
                    JdAdvertisDto jdAdvertisDto) {

        JdAdvertisBo jdAdvertisBo = JdAdvertisBo.builder().build();
        BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertisDto, jdAdvertisBo);
        return jdAdvertiseService.saveAdvertise(jdAdvertisBo);
    }

    @PostMapping("deleteAdvertise")
    public SaResult deleteAdvertise(
            @Validated(DeleteAdvertiseGroup.class )
                    JdAdvertisDto jdAdvertisDto) {

        JdAdvertisBo jdAdvertisBo = JdAdvertisBo.builder().build();
        BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertisDto, jdAdvertisBo);
        return jdAdvertiseService.deleteAdvertise(jdAdvertisBo.getJdAdId());
    }

    @PostMapping("updateAdvertise")
    public SaResult updateAdvertise(
            @Validated(UpdateAdvertiseGroup.class)
                    JdAdvertisDto jdAdvertisDto) {

        JdAdvertisBo jdAdvertisBo = JdAdvertisBo.builder().build();
        BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertisDto, jdAdvertisBo);
        return jdAdvertiseService.updateAdvertise(jdAdvertisBo);
    }

}
