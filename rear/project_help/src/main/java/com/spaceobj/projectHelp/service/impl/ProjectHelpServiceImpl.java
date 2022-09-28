package com.spaceobj.projectHelp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.projectHelp.bo.ProjectHelpBo;
import com.spaceobj.projectHelp.bo.ReceiveEmailBo;
import com.spaceobj.domain.SysProject;
import com.spaceobj.domain.SysUser;
import com.spaceobj.projectHelp.constant.KafKaTopics;
import com.spaceobj.projectHelp.constant.RedisKey;
import com.spaceobj.projectHelp.mapper.ProjectHelpMapper;
import com.spaceobj.domain.ProjectHelp;
import com.spaceobj.projectHelp.service.ProjectHelpService;
import com.spaceobj.projectHelp.service.kafka.KafkaSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class ProjectHelpServiceImpl extends ServiceImpl<ProjectHelpMapper, ProjectHelp>
    implements ProjectHelpService {
  Logger logger = LoggerFactory.getLogger(ProjectHelpServiceImpl.class);

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private ProjectHelpMapper projectHelpMapper;

  @Autowired private KafkaSender kafkaSender;

  /**
   * 根据账户获取用户信息
   *
   * @param account
   * @return
   * @throws InterruptedException
   */
  public SysUser getSysUser(String account) throws InterruptedException {
    boolean flag = redisTemplate.hasKey(RedisKey.SYS_USER_LIST);
    List<SysUser> sysUserList = null;
    SysUser sysUser = null;
    if (!flag) {
      // 刷新用户缓存信息
      kafkaSender.send(new Object(), KafKaTopics.UPDATE_USER_LIST);
      Thread.sleep(200);
      this.getSysUser(account);
    } else {
      sysUserList = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      sysUser =
          sysUserList.stream()
              .filter(
                  user -> {
                    return user.getAccount().equals(account);
                  })
              .collect(Collectors.toList())
              .get(0);
    }
    return sysUser;
  }

  /**
   * 根据用户id获取用户基本信息
   *
   * @param userId
   * @return
   */
  public SysUser getSysUserByUserId(String userId) throws InterruptedException {
    boolean flag = redisTemplate.hasKey(RedisKey.SYS_USER_LIST);
    List<SysUser> sysUserList = null;
    SysUser sysUser = null;
    if (!flag) {
      // 刷新用户缓存信息
      kafkaSender.send(new Object(), KafKaTopics.UPDATE_USER_LIST);
      Thread.sleep(200);
      this.getSysUserByUserId(userId);
    } else {
      sysUserList = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      sysUser =
          sysUserList.stream()
              .filter(
                  user -> {
                    return user.getUserId().equals(userId);
                  })
              .collect(Collectors.toList())
              .get(0);
    }
    return sysUser;
  }

  /**
   * 获取项目列表信息
   *
   * @return
   * @throws InterruptedException
   */
  public List<SysProject> getSysProjects() throws InterruptedException {
    List<SysProject> sysProjectList = null;

    boolean hasKey = redisTemplate.hasKey(RedisKey.PROJECT_LIST);
    if (!hasKey) {
      kafkaSender.send(new Object(), KafKaTopics.UPDATE_PROJECT_LIST);
      Thread.sleep(200);
      this.getSysProjects();
    } else {
      sysProjectList = redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
    }
    return sysProjectList;
  }

  public boolean getProjectHelpSyncStatus() {
    boolean hasKey = redisTemplate.hasKey(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS);
    if (!hasKey) {
      redisTemplate.opsForValue().set(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS, false);
      return false;
    } else {
      return (boolean) redisTemplate.opsForValue().get(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS);
    }
  }

  /**
   * 同步数据到Redis缓存，并返回查询到的数据
   *
   * @return
   */
  private List<ProjectHelp> getProjectHelpList() throws InterruptedException {
    List<ProjectHelp> list = null;
    boolean flag = redisTemplate.hasKey(RedisKey.PROJECT_HELP_LIST);
    if (!flag) {
      if (this.getProjectHelpSyncStatus()) {
        Thread.sleep(50);
        this.getProjectHelpList();
      } else {
        redisTemplate.opsForValue().set(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS, true);
        QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        list = projectHelpMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(RedisKey.PROJECT_HELP_LIST, list.toArray());

        redisTemplate.opsForValue().set(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS, false);
      }
    } else {
      list = redisTemplate.opsForList().range(RedisKey.PROJECT_HELP_LIST, 0, -1);
    }
    return list;
  }

  @Override
  public SaResult createProjectHelpLink(ProjectHelpBo projectHelpBo) {

    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      if (!Pattern.matches(RegexPool.EMAIL, sysUser.getEmail())) {
        return SaResult.error("请设置正确邮箱");
      }

      // 如果用户的创建剩余次数小于10次，提醒明天再来
      if (sysUser.getCreateProjectHelpTimes() <= 0) {
        return SaResult.error("今日分享链接创建已上限，明天再来吧！");
      }
      // 判断项目中是否有该项目的id
      List<SysProject> sysProjectList = getSysProjects();
      // 根据前端传递过来的项目id，判断项目列表中是否有该项目
      List<SysProject> resultSysProjectList =
          sysProjectList.stream()
              .filter(
                  p -> {
                    return p.getPId() == projectHelpBo.getPId();
                  })
              .collect(Collectors.toList());
      if (resultSysProjectList.size() == 0) {
        return SaResult.error("id错误");
      }
      SysProject sysProject = resultSysProjectList.get(0);
      if (sysProject.getStatus() != 1) {
        return SaResult.error("违规操作");
      }
      // 获取项目助力列表
      List<ProjectHelp> list = getProjectHelpList();
      // 判断用户之前是否创建过，如果没有，那么从数据库中取数据并且持久化到缓存中
      List<ProjectHelp> resultList;
      resultList =
          list.stream()
              .filter(
                  ph -> {
                    return ph.getCreateUserId().equals(sysUser.getUserId())
                        && ph.getPId() == projectHelpBo.getPId();
                  })
              .collect(Collectors.toList());
      // 如果之前创建过那么直接返回之前创建过的
      if (resultList.size() > 0) {
        return SaResult.ok().setData(resultList.get(0));
      }

      //  没有创建过，则创建
      ProjectHelp projectHelp =
          ProjectHelp.builder()
              .hpId(UUID.randomUUID().toString())
              .pId(sysProject.getPId())
              .createUserId(sysUser.getUserId())
              .hpNumber(0)
              .pContent(sysProject.getContent())
              .pPrice(sysProject.getPrice())
              .pReleaseUserId(sysProject.getReleaseUserId())
              .hpStatus(0)
              .build();
      // 消息队列通知去创建
      kafkaSender.send(projectHelp, KafKaTopics.ADD_HELP_PROJECT);
      // 消息队列通知用户服务对该用户的创建次数减一
      sysUser.setCreateProjectHelpTimes(sysUser.getCreateProjectHelpTimes() - 1);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      return SaResult.ok().setData(projectHelp);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("create project help link failed", e.getMessage());
      return SaResult.error("创建助力链接失败，服务器异常");
    }
  }

  @Override
  public SaResult updateProjectHelpNumber(ProjectHelpBo projectHelpBo) {

    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      List<ProjectHelp> list = getProjectHelpList();
      // 获取当前项目信息
      List<ProjectHelp> resultList;
      resultList =
          list.stream()
              .filter(
                  ph -> {
                    return ph.getHpId().equals(projectHelpBo.getHpId());
                  })
              .collect(Collectors.toList());
      if (resultList.size() == 0) {
        return SaResult.error("项目助力链接不存在或已失效");
      }
      ProjectHelp projectHelp = resultList.get(0);
      // 如果项目已经助力成功，那么直接返回好友已经成功
      if (projectHelp.getHpNumber() >= 10 || projectHelp.getHpStatus() == 1) {
        return SaResult.error("助力成功，快通知好友联系吧！");
      }
      if (projectHelp.getCreateUserId().equals(sysUser.getUserId())) {
        return SaResult.error("助力失败，请分享给好友助力！");
      }
      if (sysUser.getProjectHelpTimes() <= 0) {
        return SaResult.error("您今日的助力次数已经用尽，请改天再来吧");
      }
      //  用户的助力次数减少一，返回助力成功
      sysUser.setProjectHelpTimes(sysUser.getProjectHelpTimes() - 1);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      // 修改项目助力信息
      projectHelp.setHpNumber(projectHelp.getHpNumber() + 1);
      kafkaSender.send(projectHelp, KafKaTopics.UPDATE_HELP_PROJECT);
      // 如果项目助力值大于等于10，邮件通知
      if (projectHelp.getHpNumber() >= 10) {
        SysUser createSysUser = getSysUserByUserId(projectHelp.getCreateUserId());
        ReceiveEmailBo receiveEmailBo =
            ReceiveEmailBo.builder()
                .receiverEmail(createSysUser.getEmail())
                .title("项目助力成功")
                .content("项目编号：" + projectHelp.getPId() + "助力成功！快去联系吧！")
                .build();
        kafkaSender.send(receiveEmailBo, KafKaTopics.HELP_PROJECT_SUCCESSFUL);
      }
      return SaResult.ok("助力成功");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("project help failed", e.getMessage());
      return SaResult.error("项目助力失败，服务器异常！");
    }
  }

  @Override
  public SaResult projectHelpList(ProjectHelpBo projectHelpBo) {
    List<ProjectHelp> resultList = null;
    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      List<ProjectHelp> list = getProjectHelpList();
      resultList =
          list.stream()
              .filter(
                  hp -> {
                    return hp.getCreateUserId().equals(sysUser.getUserId());
                  })
              .collect(Collectors.toList());
      return SaResult.ok().setData(resultList);

    } catch (Exception e) {
      logger.error("get project help list failed", e.getMessage());
      return SaResult.error("助力列表获取失败，服务器异常！");
    }
  }
}
