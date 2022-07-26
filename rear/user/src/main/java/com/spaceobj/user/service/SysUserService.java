package com.spaceobj.user.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.user.pojo.SysUser;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
public interface SysUserService extends IService<SysUser> {

  /**
   * 查询所有的系统用户
   *
   * 必须是具有管理员权限，然后可以根据用户的ID，邮箱，微信，电话，名称查询用户，返回用户列表
   *
   * @param searchValue
   * @return
   */
  SaResult findList(String searchValue);

  /**
   * 修改系统用户: 必须具有管理员权限，然后可以冻结用户，踢下线，等操作先通过消息队列持久化，然后再刷新缓存
   *
   * @param sysUser
   * @param operateType
   * @return
   */
  SaResult updateSysUser(SysUser sysUser, Integer operateType);





}
