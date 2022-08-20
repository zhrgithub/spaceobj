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
   * 添加、修改图片，先更新到mysql然后刷新缓存
   *
   * @param photo
   * @param operation
   * @return
   */
  SaResult addOrUpdate(SysPhoto photo, Integer operation);

  /**
   * 删除图片，先删除MySQL中的数据，然后删除缓存中的
   *
   * @param id
   * @return
   */
  SaResult delete(Long id);

  /**
   * 查询全部，先从Redis中查找，如果没有的话，从MySQL中查找
   *
   * @return
   */
  SaResult photoList();



}
