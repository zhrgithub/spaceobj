package com.spaceobj.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.utils.ResultData;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
public interface SysUserService extends IService<SysUser> {

  /**
   * 查询所有的系统用户
   *
   * @return
   */
  ResultData findList();


  /**
   * 修改系统用户
   *
   * @param sysUser
   * @return
   */
  ResultData updateSysUser(SysUser sysUser);

}
