package com.spaceobj.project.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.project.bo.ProjectHelpBo;
import com.spaceobj.project.bo.SysUserBo;
import com.spaceobj.project.constent.KafKaTopics;
import com.spaceobj.project.constent.RedisKey;
import com.spaceobj.project.mapper.SysProjectMapper;
import com.spaceobj.project.pojo.SysProject;
import com.spaceobj.project.service.SysProjectService;
import com.spaceobj.project.constent.KafkaSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysProjectServiceImpl extends ServiceImpl<SysProjectMapper, SysProject>
    implements SysProjectService {

  @Autowired private SysProjectMapper sysProjectMapper;

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private KafkaSender kafkaSender;

  Logger LOG = LoggerFactory.getLogger(SysProjectServiceImpl.class);



  @Override
  public SaResult addProject(SysProject sysProject) {

    try {
      // 校验内容是否重复
      // 校验当前提交次数是否超过最大次数

      // 生成UUID
      String uuid = UUID.randomUUID().toString();
      sysProject.setUuid(uuid);
      // 设置成审核中
      sysProject.setStatus(0);
      redisTemplate.opsForList().rightPush(RedisKey.PROJECT_LIST, sysProject);

      kafkaSender.send(sysProject, KafKaTopics.ADD_PROJECT);
      return SaResult.ok().setData(sysProject);
    } catch (Exception e) {
      LOG.error("project add error", e.getMessage());
      return SaResult.error("项目新增失败");
    }
  }

  @Override
  public SaResult updateProject(SysProject sysProject) {
    try {
      if (ObjectUtils.isNotNull(sysProject.getUuid())) {

        // 从缓存中获取数据
        List<SysProject> cacheProject =
            (List<SysProject>) redisTemplate.opsForValue().get(RedisKey.PROJECT_LIST);
        // 如果当前项目是在审核中那么返回不可重复修改
        SysProject checkCacheProject =
            (SysProject)
                cacheProject.stream()
                    .filter(
                        p -> {
                          return p.getUuid().equals(sysProject.getUuid());
                        });
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
        } else {
          return SaResult.error("违规操作，修改失败");
        }
      }

      return SaResult.error("请求参数错误");
    } catch (Exception e) {
      LOG.error("update project exception", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult auditProject(SysProject project) {
    try {

      int result = sysProjectMapper.updateById(project);
      if (result == 1) {
        return SaResult.ok("审核成功");
      }
      return SaResult.error("审核失败");
    } catch (Exception e) {
      LOG.error("audit project failed", e.getMessage());
      return SaResult.error("审核失败");
    }
  }

  @Override
  public SaResult findList(
      Integer currentPage, Integer pageSize, String content, Integer projectType, String userId) {
    try {
      List<SysProject> list;
      long size = redisTemplate.opsForList().size(RedisKey.PROJECT_LIST);
      if (Long.valueOf(size) == 0) {
        QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        list = sysProjectMapper.selectList(queryWrapper);
        redisTemplate.opsForList().leftPush(RedisKey.PROJECT_LIST, list);
      } else {
        list = redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
      }

      // 查询首页信息
      if (projectType == 0) {
        list =
            (List<SysProject>)
                list.stream()
                    .filter(
                        p -> {
                          if (ObjectUtils.isNotNull(content)) {
                            return p.getStatus() == 1 && p.getContent().contains(content);

                          } else {
                            return p.getStatus() == 1;
                          }
                        });
      } else if (projectType == 1) {
        // 查询自己发布的信息
        list =
            (List<SysProject>)
                list.stream()
                    .filter(
                        p -> {
                          return p.getReleaseUserId().equals(userId);
                        });
      } else if (projectType == 2) {
        // 管理员查询信息全部的信息
        list =
            (List<SysProject>)
                list.stream()
                    .filter(
                        p -> {
                          if (ObjectUtils.isNotNull(content)) {
                            return Long.valueOf(content).equals(p.getPId())
                                || p.getContent().contains(content);
                          }
                          return true;
                        });
      } else {
        return SaResult.error("请求参数错误");
      }

      return SaResult.ok().setData(list);
    } catch (Exception e) {
      LOG.error("system project find error", e.getMessage());
      return SaResult.error("项目列表查询结果异常");
    }
  }

  @Override
  public void addPageViews(String projectId) {
    try {
      SysProject sysProject = SysProject.builder().pId(Long.valueOf(projectId)).build();
      kafkaSender.send(sysProject, KafKaTopics.VIEWS_PROJECT);
    } catch (Exception e) {
      LOG.error("add Page view error", e.getMessage());
    }
  }

  @Override
  public SaResult getPhoneNumberByProjectId(String projectId, String userId) {
    try {
      List<SysProject> list;
      SysProject sysProject;
      long size = redisTemplate.opsForList().size(RedisKey.PROJECT_LIST);
      if (size == 0) {
        QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
        list = sysProjectMapper.selectList(queryWrapper);
        redisTemplate.opsForList().leftPush(RedisKey.PROJECT_LIST, list);
      } else {
        list = redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
      }

      sysProject =
          (SysProject)
              list.stream()
                  .filter(
                      p -> {
                        return Long.valueOf(projectId).equals(p.getPId());
                      });

      List<SysUserBo> sysUserBos = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      List<SysUserBo> resultSysUserBos =
          (List<SysUserBo>)
              sysUserBos.stream()
                  .filter(
                      user -> {
                        return user.getUserId().equals(userId);
                      });
      SysUserBo sysUserBo = resultSysUserBos.get(0);
      // 如果项目发布人id和userId相同，直接返回用户联系方式
      if (sysProject.getReleaseUserId().equals(userId)) {
        return SaResult.ok().setData(sysUserBo.getPhoneNumber());
      }
      //  判断当前用户是否已经实名认证
      if (!(sysUserBo.getRealNameStatus() == 1)) {
        return SaResult.error("请实名认证后再来获取");
      }
      //  判断项目是否审核通过
      if (!(sysProject.getStatus() == 1)) {
        return SaResult.error("项目未通过审核，无法获取");
      }

      // 判断该用户的助力列表中是否有该项目数据
      // 判断是否已经获取到该项目联系人
      List<ProjectHelpBo> projectHelpBoList =
          redisTemplate.opsForList().range(RedisKey.PROJECT_HELP_LIST, 0, -1);
      projectHelpBoList.stream()
          .filter(
              hp -> {
                return hp.getCreateUserId().equals(userId)
                    && Long.valueOf(projectId).equals(hp.getPId());
              });
      if (projectHelpBoList.size() > 0) {
        ProjectHelpBo helpBo = projectHelpBoList.get(0);
        if (helpBo.getHpStatus() == 1) {
          return SaResult.ok().setData(sysUserBo.getPhoneNumber());
        }
      }

      // 判断用户的邀请值大于0
      if (sysUserBo.getInvitationValue() > 0) {
        //  邀请值减一，如果项目助力列表中没有该项目，那么设置成已经获取到，如果没有，那么新增到项目助力列表并设置成已经获取到的状态
        sysUserBo.setInvitationValue(sysUserBo.getInvitationValue() - 1);
        ProjectHelpBo helpBo;
        if (projectHelpBoList.size() > 0) {
          helpBo = projectHelpBoList.get(0);
          helpBo.setHpNumber(10);
          // 通知项目助力服务更新数据
          kafkaSender.send(helpBo, KafKaTopics.UPDATE_HELP_PROJECT);
        } else {
          helpBo =
              ProjectHelpBo.builder()
                  .hpId(UUID.randomUUID().toString())
                  .pId(sysProject.getPId())
                  .createUserId(userId)
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
        kafkaSender.send(sysUserBo, KafKaTopics.UPDATE_USER);

        return SaResult.ok().setData(sysUserBo.getPhoneNumber());
      }
      return SaResult.error("请分享项目助力链接获取");
    } catch (Exception e) {
      LOG.error("get phone number by projectId error", e.getMessage());
      return SaResult.error("获取联系方式失败，服务器异常");
    }
  }
}
