package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.constent.RedisKey;
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

  @Autowired private SysUserMapper sysUserMapper;

  @Autowired private RedisTemplate redisTemplate;



  private static final Logger LOG = LoggerFactory.getLogger(SysUserServiceImpl.class);

  @Override
  public SaResult findList(String searchValue) {
    List<SysUser> list = null;
    try {
      Long size = redisTemplate.opsForList().size(RedisKey.SYS_USER_LIST);
      if (size == 0) {
        QueryWrapper queryWrapper = new QueryWrapper();
        list = sysUserMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(RedisKey.SYS_USER_LIST, list.toArray());
      } else {
        // 此处建议使用pipeLine来提升性能
        list = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
      return SaResult.error(e.getMessage());
    }
    return SaResult.ok().setData(list);
  }

  @Override
  public SaResult updateSysUser(SysUser sysUser, Integer operateType) {
    Integer result = -1;
    try {
      result = sysUserMapper.updateById(sysUser);
      redisTemplate.delete(RedisKey.SYS_USER_LIST);
    } catch (RuntimeException e) {
      LOG.error("updateSysUSer failed", e.getMessage());
      return SaResult.error(e.getMessage()).setData(result);
    }

    return SaResult.ok().setData(result);
  }
}
