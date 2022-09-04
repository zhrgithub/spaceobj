package com.spaceobj.projectHelp.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.projectHelp.bo.ProjectHelpBo;
import com.spaceobj.projectHelp.bo.ReceiveEmailBo;
import com.spaceobj.projectHelp.bo.SysProjectBo;
import com.spaceobj.projectHelp.bo.SysUserBo;
import com.spaceobj.projectHelp.constent.KafKaTopics;
import com.spaceobj.projectHelp.constent.RedisKey;
import com.spaceobj.projectHelp.mapper.ProjectHelpMapper;
import com.spaceobj.projectHelp.pojo.ProjectHelp;
import com.spaceobj.projectHelp.service.ProjectHelpService;
import com.spaceobj.projectHelp.service.kafka.KafkaSender;
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
public class SysProjectHelpServiceImpl extends ServiceImpl<ProjectHelpMapper, ProjectHelp>
    implements ProjectHelpService {
  Logger LOG = LoggerFactory.getLogger(SysProjectHelpServiceImpl.class);

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private ProjectHelpMapper projectHelpMapper;

  @Autowired private KafkaSender kafkaSender;

  @Override
  public SaResult createProjectHelpLink(ProjectHelpBo projectHelpBo) {

    try {
      // 如果用户的创建剩余次数小于10次，提醒明天再来
      List<SysUserBo> sysUserBoList =
          redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      List<SysUserBo> resultSysUserBoList =
          (List<SysUserBo>)
              sysUserBoList.stream()
                  .filter(
                      u -> {
                        return u.getUserId().equals(projectHelpBo.getUserId());
                      });
      if (resultSysUserBoList.size() == 0) {
        return SaResult.error("用户不存在");
      }
      SysUserBo sysUserBo = resultSysUserBoList.get(0);
      if (sysUserBo.getCreateProjectHelpTimes() <= 0) {
        return SaResult.error("今日分享链接创建已上限，明天再来吧！");
      }
      // 判断项目中是否有该项目的id
      List<SysProjectBo> sysProjectBoList =
          redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
      List<SysProjectBo> resultSysProjectBoList =
          (List<SysProjectBo>)
              sysProjectBoList.stream()
                  .filter(
                      p -> {
                        return p.getPId() == projectHelpBo.getPId();
                      });
      if (resultSysProjectBoList.size() == 0) {
        return SaResult.error("项目id不正确");
      }

      List<ProjectHelp> list = redisTemplate.opsForList().range(RedisKey.PROJECT_HELP_LIST, 0, -1);
      // 判断用户之前是否创建过，如果没有，那么从数据库中取数据并且持久化到缓存中
      if (list.size() < 0) {
        synchronized (this) {
          QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper<>();
          list = projectHelpMapper.selectList(queryWrapper);
          redisTemplate.opsForList().leftPush(RedisKey.PROJECT_HELP_LIST, list.toArray());
        }
      }
      List<ProjectHelp> resultList;
      resultList =
          (List<ProjectHelp>)
              list.stream()
                  .filter(
                      ph -> {
                        return ph.getPReleaseUserId().equals(projectHelpBo.getUserId())
                            && ph.getPId() == projectHelpBo.getPId();
                      });
      // 如果之前创建过那么直接返回之前创建过的
      if (resultList.size() > 0) {
        return SaResult.ok().setData(resultList.get(0));
      }

      //  没有创建过，则创建
      ProjectHelp projectHelp =
          ProjectHelp.builder()
              .hpId(UUID.randomUUID().toString())
              .pId(projectHelpBo.getPId())
              .createUserId(projectHelpBo.getUserId())
              .hpNumber(0)
              .pContent(projectHelpBo.getPContent())
              .pPrice(projectHelpBo.getPPrice())
              .pReleaseUserId(projectHelpBo.getPReleaseUserId())
              .hpStatus(0)
              .build();
      // 消息队列通知去创建
      kafkaSender.send(projectHelp, KafKaTopics.ADD_HELP_PROJECT);
      return SaResult.ok().setData(projectHelp);
    } catch (Exception e) {
      LOG.error("create project help link failed", e.getMessage());
      return SaResult.error("创建助力链接失败，服务器异常");
    }
  }

  @Override
  public SaResult updateProjectHelpNumber(ProjectHelpBo projectHelpBo) {

    try {
      List<ProjectHelp> list = redisTemplate.opsForList().range(RedisKey.PROJECT_HELP_LIST, 0, -1);
      // 获取当前项目信息
      List<ProjectHelp> resultList;
      resultList =
          (List<ProjectHelp>)
              list.stream()
                  .filter(
                      ph -> {
                        return ph.getPId() == projectHelpBo.getPId();
                      });
      ProjectHelp projectHelp = resultList.get(0);
      // 如果项目已经助力成功，那么直接返回好友已经成功
      if (projectHelp.getHpStatus() == 1) {
        return SaResult.error("助力成功，快通知好友联系吧！");
      }
      if (projectHelp.getCreateUserId().equals(projectHelpBo.getUserId())) {
        return SaResult.error("助力失败，请分享给好友助力！");
      }
      List<SysUserBo> sysUserBoList =
          redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      List<SysUserBo> resultSysUserBo =
          (List<SysUserBo>)
              sysUserBoList.stream()
                  .filter(
                      user -> {
                        return user.getUserId().equals(projectHelpBo.getUserId());
                      });
      SysUserBo sysUserBo = resultSysUserBo.get(0);
      if (sysUserBo.getProjectHelpTimes() <= 0) {
        return SaResult.error("您今日的助力次数已经用尽，请改天再来吧");
      }
      //  用户的助力次数减少一，返回助力成功
      sysUserBo.setProjectHelpTimes(sysUserBo.getProjectHelpTimes() - 1);
      kafkaSender.send(sysUserBo, KafKaTopics.UPDATE_USER);
      // 修改项目助力信息
      projectHelp.setHpNumber(projectHelp.getHpNumber() + 1);
      kafkaSender.send(projectHelp, KafKaTopics.UPDATE_HELP_PROJECT);
      // 如果项目大于等于10，邮件通知
      if (projectHelp.getHpNumber() >= 10) {
        List<SysUserBo> createProjectUserList =
            (List<SysUserBo>)
                sysUserBoList.stream()
                    .filter(
                        user -> {
                          return user.getUserId().equals(projectHelp.getCreateUserId());
                        });
        SysUserBo createSysUserBo = createProjectUserList.get(0);
        ReceiveEmailBo receiveEmailBo =
            ReceiveEmailBo.builder()
                .receiverEmail(createSysUserBo.getAccount())
                .title("项目助力成功")
                .content("项目编号：" + projectHelp.getPId() + "助力成功！快去联系吧！")
                .build();
        kafkaSender.send(receiveEmailBo, KafKaTopics.HELP_PROJECT_SUCCESSFUL);
      }
      return SaResult.ok("助力成功").setData(sysUserBo);
    } catch (Exception e) {

      LOG.error("project help failed", e.getMessage());
      return SaResult.error("项目助力失败，服务器异常！");
    }
  }

  @Override
  public SaResult projectHelpList(ProjectHelpBo projectHelpBo) {

    List<ProjectHelp> resultList = null;

    try {
      List<ProjectHelp> list = null;
      long size = redisTemplate.opsForList().size(RedisKey.PROJECT_HELP_LIST);
      // 如果缓存中的数据为0，那么从数据库中查询，并缓存到Redis中
      if (size == 0) {
        synchronized (this) {
          QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper<>();
          list = projectHelpMapper.selectList(queryWrapper);
        }
        redisTemplate.opsForList().leftPush(RedisKey.PROJECT_HELP_LIST, list.toArray());
        resultList =
            (List<ProjectHelp>)
                list.stream()
                    .filter(
                        ph -> {
                          return ph.getPReleaseUserId().equals(projectHelpBo.getUserId());
                        });

      } else {
        //  否则从缓存中查找
        list = redisTemplate.opsForList().range(RedisKey.PROJECT_HELP_LIST, 0, -1);
        resultList =
            (List<ProjectHelp>)
                list.stream()
                    .filter(
                        hp -> {
                          return hp.getPReleaseUserId().equals(projectHelpBo.getUserId());
                        });
      }
      return SaResult.ok().setData(resultList);

    } catch (Exception e) {
      LOG.error("get project help list failed", e.getMessage());
      return SaResult.error("助力列表获取失败，服务器异常！");
    }
  }
}
