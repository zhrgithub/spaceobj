package com.spaceobj.project.service.impl;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.domain.ProjectHelp;
import com.spaceobj.domain.SysProject;
import com.spaceobj.domain.SysUser;
import com.spaceobj.project.bo.GetPhoneNumberBo;
import com.spaceobj.project.bo.ProjectSearchBo;
import com.spaceobj.project.constant.KafKaTopics;
import com.spaceobj.project.constant.KafkaSender;
import com.spaceobj.project.constant.RedisKey;
import com.spaceobj.project.mapper.SysProjectMapper;
import com.spaceobj.project.service.SysProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysProjectServiceImpl extends ServiceImpl<SysProjectMapper, SysProject>
    implements SysProjectService {

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private KafkaSender kafkaSender;

  Logger LOG = LoggerFactory.getLogger(SysProjectServiceImpl.class);

  @Override
  public SaResult addProject(SysProject sysProject) {

    try {
      // 校验内容是否重复
      List<SysProject> sysProjectList =
          redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);

      List<SysProject> resultSysProjectList =
          sysProjectList.stream()
              .filter(
                  p -> {
                    return p.getContent().equals(sysProject.getContent());
                  })
              .collect(Collectors.toList());
      if (resultSysProjectList.size() > 0) {
        return SaResult.error("请勿重复提交");
      }

      // 校验当前提交次数是否超过最大次数
      List<SysUser> sysUserList = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      List<SysUser> resultSysUser =
          sysUserList.stream()
              .filter(
                  u -> {
                    return u.getUserId().equals(sysProject.getReleaseUserId());
                  })
              .collect(Collectors.toList());
      if (resultSysUser.size() == 0) {
        return SaResult.error("用户不存在");
      }
      SysUser sysUser = resultSysUser.get(0);
      if (sysUser.getReleaseProjectTimes() <= 0) {
        return SaResult.error("今天发布次数已上线，明天再来吧！");
      }
      // 修改用户信息
      sysUser.setReleaseProjectTimes(sysUser.getReleaseProjectTimes() - 1);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);

      // 生成UUID
      String uuid = UUID.randomUUID().toString();
      sysProject.setUuid(uuid);
      // 设置成审核中
      sysProject.setStatus(0);
      redisTemplate.opsForList().rightPush(RedisKey.PROJECT_LIST, sysProject);

      kafkaSender.send(sysProject, KafKaTopics.ADD_PROJECT);
      return SaResult.ok().setData(sysProject);
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("project add error", e.getMessage());
      return SaResult.error("项目新增失败，服务器异常");
    }
  }

  @Override
  public SaResult updateProject(SysProject sysProject) {
    try {
      if (ObjectUtils.isNotNull(sysProject.getUuid())) {

        // 从缓存中获取数据
        List<SysProject> cacheProject =
            (List<SysProject>) redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
        // 如果当前项目是在审核中那么返回不可重复修改
        List<SysProject> checkCacheProjectList =
            cacheProject.stream()
                .filter(
                    p -> {
                      return p.getUuid().equals(sysProject.getUuid());
                    })
                .collect(Collectors.toList());
        if (checkCacheProjectList.size() == 0) {
          return SaResult.error("项目不存在");
        }
        SysProject checkCacheProject = checkCacheProjectList.get(0);
        if (!ObjectUtils.isNotNull(checkCacheProject)) {
          return SaResult.error("项目不存在");
        }

        if (checkCacheProject.getStatus() == 0) {
          return SaResult.error("审核中，不可重复提交修改");
        }

        if (checkCacheProject.getStatus() == 1) {

          // 修改的项目设置成待审核
          sysProject.setStatus(0);

          //  刷新缓存中的数据为审核状态
          cacheProject.stream()
              .forEach(
                  k -> {
                    if (k.getUuid().equals(sysProject.getUuid())) {
                      k.setStatus(0);
                    }
                  });
          redisTemplate.delete(RedisKey.PROJECT_LIST);
          redisTemplate.opsForList().leftPush(RedisKey.PROJECT_LIST, cacheProject);
          //  发送消息队列持久化修改数据
          kafkaSender.send(sysProject, KafKaTopics.UPDATE_PROJECT);
          return SaResult.ok("已提交审核");
        } else {
          return SaResult.error("违规操作，修改失败");
        }
      }

      return SaResult.error("请求参数错误");
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("update project exception", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult auditProject(SysProject project) {
    try {
      kafkaSender.send(project, KafKaTopics.UPDATE_PROJECT);
      return SaResult.error("审核已提交，刷新列表查看结果");
    } catch (Exception e) {
      LOG.error("audit project failed", e.getMessage());
      return SaResult.error("审核失败");
    }
  }

  @Override
  public SaResult findList(ProjectSearchBo projectSearchBo) {
    try {

      List<SysProject> list;
      long size = redisTemplate.opsForList().size(RedisKey.PROJECT_LIST);
      if (size == 0) {
        kafkaSender.send(new Object(), KafKaTopics.UPDATE_PROJECT_LIST);
        return SaResult.error("系统项目数据同步中，请稍后");
      } else {
        list = redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
      }

      // 查询首页信息
      if (projectSearchBo.getProjectType() == 0) {
        list =
            list.stream()
                .filter(
                    p -> {
                      if (ObjectUtils.isNotNull(projectSearchBo.getContent())) {
                        return p.getStatus() == 1
                            && p.getContent().contains(projectSearchBo.getContent());
                      } else {
                        return p.getStatus() == 1;
                      }
                    })
                .collect(Collectors.toList());
      } else if (projectSearchBo.getProjectType() == 1) {
        if (StringUtils.isEmpty(projectSearchBo.getUserId())) {
          return SaResult.error("用户id不为空");
        }
        // 查询自己发布的信息
        list =
            list.stream()
                .filter(
                    p -> {
                      return p.getReleaseUserId().equals(projectSearchBo.getUserId());
                    })
                .collect(Collectors.toList());
      } else if (projectSearchBo.getProjectType() == 2) {
        // 管理员查询信息全部的信息
        list =
            list.stream()
                .filter(
                    p -> {
                      if (ObjectUtils.isNotNull(projectSearchBo.getContent())) {
                        // 如果是项目编号，匹配项目编号然后返回
                        if (Pattern.matches(RegexPool.NUMBERS, projectSearchBo.getContent())) {
                          return Long.valueOf(projectSearchBo.getContent()).longValue()
                              == p.getPId();
                        }
                        // 如果是内容，匹配内容
                        return p.getContent().contains(projectSearchBo.getContent());
                      }
                      return true;
                    })
                .collect(Collectors.toList());
      } else {
        return SaResult.error("请求参数错误");
      }

      return SaResult.ok().setData(list);
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("system project find error", e.getMessage());
      return SaResult.error("项目列表查询结果异常");
    }
  }

  @Override
  public void addPageViews(long projectId) {
    try {
      List<SysProject> sysProjectList =
          redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
      List<SysProject> resultSysProject =
          sysProjectList.stream().filter(p -> p.getPId() == projectId).collect(Collectors.toList());
      if (resultSysProject.size() == 0) {
        return;
      }
      SysProject sysProject = resultSysProject.get(0);
      sysProject.setPageViews(sysProject.getPageViews() + 1);
      kafkaSender.send(sysProject, KafKaTopics.UPDATE_PROJECT);
    } catch (Exception e) {
      LOG.error("add Page view error", e.getMessage());
    }
  }

  @Override
  public SaResult getPhoneNumberByProjectId(GetPhoneNumberBo getPhoneNumberBo) {
    try {
      List<SysProject> list;
      List<SysProject> sysProjectList;
      long size = redisTemplate.opsForList().size(RedisKey.PROJECT_LIST);
      if (size == 0) {
        kafkaSender.send(new Object(), KafKaTopics.UPDATE_PROJECT_LIST);
        return SaResult.error("系统项目同步中，请稍后");
      } else {
        list = redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
      }

      sysProjectList =
          list.stream()
              .filter(
                  p -> {
                    return getPhoneNumberBo.getProjectId() == p.getPId();
                  })
              .collect(Collectors.toList());
      if (sysProjectList.size() == 0) {
        return SaResult.error("项目不存在");
      }
      SysProject sysProject = sysProjectList.get(0);

      List<SysUser> sysUsers = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      List<SysUser> resultSysUsers =
          sysUsers.stream()
              .filter(
                  user -> {
                    return user.getUserId().equals(getPhoneNumberBo.getUserId());
                  })
              .collect(Collectors.toList());
      if (resultSysUsers.size() == 0) {
        return SaResult.error("用户不存在");
      }
      SysUser sysUser = resultSysUsers.get(0);
      // 如果项目发布人id和userId相同，直接返回用户联系方式
      if (sysProject.getReleaseUserId().equals(getPhoneNumberBo.getUserId())) {
        return SaResult.ok().setData(sysUser.getPhoneNumber());
      }
      //  判断当前用户是否已经实名认证
      if (sysUser.getRealNameStatus() != 1) {
        return SaResult.error("请实名认证后再来获取");
      }
      //  判断项目是否审核通过
      if (sysProject.getStatus() != 1) {
        return SaResult.error("项目未通过审核，无法获取");
      }

      // 判断该用户的助力列表中是否有该项目数据
      // 判断是否已经获取到该项目联系人
      List<ProjectHelp> projectHelpList =
          redisTemplate.opsForList().range(RedisKey.PROJECT_HELP_LIST, 0, -1);
      List<ProjectHelp> resultProjectHelp =
          projectHelpList.stream()
              .filter(
                  hp -> {
                    return hp.getCreateUserId().equals(getPhoneNumberBo.getUserId())
                        && getPhoneNumberBo.getProjectId() == hp.getPId();
                  })
              .collect(Collectors.toList());
      if (resultProjectHelp.size() > 0) {
        ProjectHelp helpBo = resultProjectHelp.get(0);
        if (helpBo.getHpStatus() == 1||helpBo.getHpNumber()>=10) {
          return SaResult.ok().setData(sysUser.getPhoneNumber());
        }
      }

      // 判断用户的邀请值大于0
      if (sysUser.getInvitationValue() > 0) {
        //  邀请值减一，如果项目助力列表中没有该项目，那么设置成已经获取到，如果没有，那么新增到项目助力列表并设置成已经获取到的状态
        sysUser.setInvitationValue(sysUser.getInvitationValue() - 1);
        ProjectHelp helpBo;
        if (resultProjectHelp.size() > 0) {
          helpBo = resultProjectHelp.get(0);
          helpBo.setHpNumber(10);
          helpBo.setHpStatus(1);
          // 通知项目助力服务更新数据
          kafkaSender.send(helpBo, KafKaTopics.UPDATE_HELP_PROJECT);
        } else {
          helpBo =
              ProjectHelp.builder()
                  .hpId(UUID.randomUUID().toString())
                  .pId(sysProject.getPId())
                  .createUserId(getPhoneNumberBo.getUserId())
                  .hpNumber(10)
                  .pContent(sysProject.getContent())
                  .pPrice(sysProject.getPrice())
                  .pReleaseUserId(sysProject.getReleaseUserId())
                  .hpStatus(1)
                  .build();
          // 通知项目助力服务新增数据
          kafkaSender.send(helpBo, KafKaTopics.ADD_HELP_PROJECT);
        }
        // 消息队列发送用户信息
        kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);

        return SaResult.ok().setData(sysUser.getPhoneNumber());
      }
      return SaResult.error("请分享项目助力链接获取");
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("get phone number by projectId error", e.getMessage());
      return SaResult.error("获取联系方式失败，服务器异常");
    }
  }
}
