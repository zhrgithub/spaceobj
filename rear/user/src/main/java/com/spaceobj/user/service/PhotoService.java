package com.spaceobj.user.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.user.pojo.SysPhoto;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 23:49
 */
public interface PhotoService extends IService<SysPhoto> {

  /**
   * 添加、修改图片
   *
   * @param photo
   * @param operation
   * @return
   */
  SaResult addOrUpdate(SysPhoto photo, Integer operation);

  /**
   * 删除图片
   *
   * @param id
   * @return
   */
  SaResult delete(Long id);

  /**
   * 查询全部
   *
   * @return
   */
  SaResult photoList();



}
