package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.core.utils.BeanConvertToTargetUtils;
import com.spaceobj.user.bo.JdAdvertiseBo;
import com.spaceobj.user.dto.JdAdvertiseDto;
import com.spaceobj.user.group.advertise.DeleteAdvertiseGroup;
import com.spaceobj.user.group.advertise.InsertAdverTiseGroup;
import com.spaceobj.user.group.advertise.UpdateAdvertiseGroup;
import com.spaceobj.user.service.JdAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhr_java@163.com
 * @date 2022/7/17 22:50
 */
@RestController
@RequestMapping(value = "jd", method = RequestMethod.POST)
public class JdAdvertiseController {

  @Autowired private JdAdvertiseService jdAdvertiseService;

  @GetMapping(value = "list")
  public SaResult list() {

    return jdAdvertiseService.findList();
  }

  @PostMapping("saveAdvertise")
  public SaResult saveAdvertise(
      @Validated(InsertAdverTiseGroup.class) JdAdvertiseDto jdAdvertiseDto) {

    JdAdvertiseBo jdAdvertiseBo = JdAdvertiseBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertiseDto, jdAdvertiseBo);
    return jdAdvertiseService.saveAdvertise(jdAdvertiseBo);
  }

  @PostMapping("deleteAdvertise")
  public SaResult deleteAdvertise(
      @Validated(DeleteAdvertiseGroup.class) JdAdvertiseDto jdAdvertiseDto) {

    JdAdvertiseBo jdAdvertiseBo = JdAdvertiseBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertiseDto, jdAdvertiseBo);
    return jdAdvertiseService.deleteAdvertise(jdAdvertiseBo.getJdAdId());
  }

  @PostMapping("updateAdvertise")
  public SaResult updateAdvertise(
      @Validated(UpdateAdvertiseGroup.class) JdAdvertiseDto jdAdvertiseDto) {

    JdAdvertiseBo jdAdvertiseBo = JdAdvertiseBo.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertiseDto, jdAdvertiseBo);
    return jdAdvertiseService.updateAdvertise(jdAdvertiseBo);
  }
}
