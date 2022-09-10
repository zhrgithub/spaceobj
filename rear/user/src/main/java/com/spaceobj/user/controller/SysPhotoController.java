package com.spaceobj.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.bo.SysPhotoBo;
import com.spaceobj.user.dto.SysPhotoDto;
import com.spaceobj.user.group.photo.AddOrUpdatePhotoGroup;
import com.spaceobj.user.group.photo.DeletePhotoGroup;
import com.spaceobj.user.service.PhotoService;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhr_java@163.com
 * @date 2022/9/4 19:17
 */
@RestController("sysPhotoController")
@RequestMapping(value = "/sysPhoto", method = RequestMethod.POST)
public class SysPhotoController {

  @Autowired private PhotoService photoService;

  @PostMapping("addOrUpdate")
  public SaResult addOrUpdate(@Validated(AddOrUpdatePhotoGroup.class) SysPhotoDto sysPhotoDto) {
    SysPhotoBo sysPhotoBo = new SysPhotoBo();
    BeanConvertToTargetUtils.copyNotNullProperties(sysPhotoDto, sysPhotoBo);
    return photoService.addOrUpdate(sysPhotoBo);
  }

  @PostMapping("delete")
  public SaResult delete(@Validated(DeletePhotoGroup.class) SysPhotoDto sysPhotoDto) {
    SysPhotoBo sysPhotoBo = new SysPhotoBo();
    BeanConvertToTargetUtils.copyNotNullProperties(sysPhotoDto, sysPhotoBo);
    return photoService.delete(sysPhotoBo);
  }

  @PostMapping("photoList")
  public SaResult photoList() {
    return photoService.photoList();
  }
}
