package com.spaceobj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
import com.spaceobj.user.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

  @Autowired
  private SysUserMapper sysUserMapper;

  @Autowired
  private RedisTemplate redisTemplate;

  /** 系统用户列表 */
  private static final String SysUserList = "sys_user_list";

  private static final Logger LOG = LoggerFactory.getLogger(SysUserServiceImpl.class);

  @Override
  public ResultData findList() {
    List<SysUser> list = null;
    try {
      Long size = redisTemplate.opsForList().size(SysUserList);
      if (size == 0) {
        QueryWrapper queryWrapper = new QueryWrapper();
        list = sysUserMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll("sys_user_list", list.toArray());
      } else {
        //可以使用pipeLine来提升性能
        list = redisTemplate.opsForList().range(SysUserList, 0, -1);
      }
    } catch (Exception e) {
      LOG.error("查询用户数据异常");
      e.printStackTrace();
      return ResultData.error("用户服务器异常");
    }
    return ResultData.success().setData("sys_user_list", list);
  }


  @Override
  public ResultData updateSysUser(SysUser sysUser) {

    return null;
  }
}
