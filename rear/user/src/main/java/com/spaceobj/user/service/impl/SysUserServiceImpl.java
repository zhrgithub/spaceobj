package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
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
  private static final String SYS_USER_LIST = "sys_user_list";

  private static final Logger LOG = LoggerFactory.getLogger(SysUserServiceImpl.class);

  @Override
  public SaResult findList(String searchValue) {
    List<SysUser> list = null;
    try {
      Long size = redisTemplate.opsForList().size(SYS_USER_LIST);
      if (size == 0) {
        QueryWrapper queryWrapper = new QueryWrapper();
        list = sysUserMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(SYS_USER_LIST, list.toArray());
      } else {
        //可以使用pipeLine来提升性能
        list = redisTemplate.opsForList().range(SYS_USER_LIST, 0, -1);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
      return SaResult.error(e.getMessage());
    }
    return SaResult.ok().setData(list);
  }


  @Override
  public SaResult updateSysUser(SysUser sysUser, Integer operateType) {

    return null;
  }

}
