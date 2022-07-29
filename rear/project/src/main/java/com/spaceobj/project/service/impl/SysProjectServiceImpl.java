package com.spaceobj.project.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.project.mapper.SysProjectMapper;
import com.spaceobj.project.pojo.SysProject;
import com.spaceobj.project.service.SysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysProjectServiceImpl extends ServiceImpl<SysProjectMapper, SysProject>
    implements SysProjectService {

  @Autowired
  private SysProjectMapper sysProjectMapper;

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public SaResult addProject(SysProject sysProject) {

    return null;
  }

  @Override
  public SaResult updateProject(SysProject sysProject) {

    return null;
  }

  @Override
  public SaResult findList(String content) {

    return null;
  }

  @Override
  public SaResult findListByReleaseUserId(String releaseUserId) {

    return null;
  }

  @Override
  public SaResult addPageViews(String projectId) {

    return null;
  }

  @Override
  public SaResult getPhoneNumberByProjectId(String projectId) {

    return null;
  }

  @Override
  public SaResult auditProject(SysProject sysProject) {

    return null;
  }

  @Override
  public SaResult findListForAdmin(SysProject sysProject) {

    return null;
  }

}
