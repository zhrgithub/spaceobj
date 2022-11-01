package com.spaceobj.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.common.service.RedisService;
import com.redis.common.service.RedissonService;
import com.spaceobj.project.bo.ProjectHelpBo;
import com.spaceobj.project.bo.ReceiveEmailBo;
import com.spaceobj.project.component.KafkaSender;
import com.spaceobj.project.component.UserClient;
import com.spaceobj.project.constant.KafKaTopics;
import com.spaceobj.project.constant.RedisKey;
import com.spaceobj.project.mapper.ProjectHelpMapper;
import com.spaceobj.project.pojo.ProjectHelp;
import com.spaceobj.project.pojo.SysProject;
import com.spaceobj.project.pojo.SysUser;
import com.spaceobj.project.service.ProjectHelpService;
import com.spaceobj.project.service.SysProjectService;
import com.spaceobj.project.util.ExceptionUtil;
import com.spaceobj.project.util.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
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

  @Autowired private RedisService redisService;

  @Autowired private RedissonService redissonService;

  @Autowired private ProjectHelpMapper projectHelpMapper;

  @Autowired private KafkaSender kafkaSender;

  @Value("${privateKey}")
  private String privateKey;

  @Resource private UserClient userClient;

  @Autowired private SysProjectService sysProjectService;

  /**
   * 根据账户获取用户信息，异常则返回null
   *
   * @param account
   * @return
   * @throws InterruptedException
   */
  public SysUser getSysUser(String account) {
    SysUser sysUser = null;
    try {
      boolean flag = redisService.hasKey(RedisKey.SYS_USER_LIST);
      if (flag) {
        sysUser = redisService.getCacheMapValue(RedisKey.SYS_USER_LIST, account, SysUser.class);
        if (!ObjectUtils.isEmpty(sysUser)) {
          return sysUser;
        }
      }
      Object res = userClient.getUserInfoByAccount(account);
      sysUser = RsaUtils.decryptByPrivateKey(res, SysUser.class, privateKey);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      return null;
    }
    return sysUser;
  }

  public SysUser getSysUserByUserId(String userId) {
    SysUser sysUser = null;
    try {
      boolean flag = redisService.hasKey(RedisKey.SYS_USER_LIST);
      if (flag) {
        List<SysUser> sysUserList =
            redisService.getCacheList(RedisKey.SYS_USER_LIST, SysUser.class);
        List<SysUser> resultSysUserList =
            sysUserList.stream()
                .filter(
                    u -> {
                      return !ObjectUtils.isEmpty(u) && u.getUserId().equals(userId);
                    })
                .collect(Collectors.toList());
        if (resultSysUserList.size() > 0) {
          return resultSysUserList.get(0);
        }
      }
      Object res = userClient.getSysUserByUserId(userId);
      sysUser = RsaUtils.decryptByPrivateKey(res, SysUser.class, privateKey);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return null;
    }
    return sysUser;
  }

  /**
   * 同步数据到Redis缓存，并返回查询到的数据
   *
   * @return
   */
  private List<ProjectHelp> getProjectHelpList() {
    List<ProjectHelp> resultList = null;

    try {
      boolean hasKey = redisService.hasKey(RedisKey.PROJECT_HELP_LIST);
      if (hasKey) {
        resultList = redisService.getHashMapValues(RedisKey.PROJECT_HELP_LIST, ProjectHelp.class);
        return resultList;
      } else {
        boolean flag = redissonService.tryLock(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS);
        if (!flag) {
          return null;
        } else {
          // 再次查询缓存，存在则返回
          hasKey = redisService.hasKey(RedisKey.PROJECT_HELP_LIST);
          if (hasKey) {
            resultList =
                redisService.getHashMapValues(RedisKey.PROJECT_HELP_LIST, ProjectHelp.class);
            return resultList;
          }
          QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper<>();
          queryWrapper.orderByDesc("create_time");
          resultList = projectHelpMapper.selectList(queryWrapper);
          // 缓存同步
          for (ProjectHelp p : resultList) {
            redisService.setCacheMapValue(RedisKey.PROJECT_HELP_LIST, p.getHpId(), p);
          }
          return resultList;
        }
      }
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public SaResult createProjectHelpLink(ProjectHelpBo projectHelpBo) {

    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      if (StringUtils.isEmpty(sysUser.getEmail())
          || StringUtils.isEmpty(sysUser.getPhoneNumber())) {
        return SaResult.error("请到个人中心设置邮箱和联系电话");
      }
      if (!Pattern.matches(RegexPool.EMAIL, sysUser.getEmail())) {
        return SaResult.error("请到个人中心设置邮箱和联系电话");
      }

      // 如果用户的创建剩余次数小于10次，提醒明天再来
      if (sysUser.getCreateProjectHelpTimes() <= 0) {
        return SaResult.error("今日分享链接创建已上限，明天再来吧！");
      }
      // 判断项目中是否有该项目的id
      // 根据前端传递过来的项目id，判断项目列表中是否有该项目
      SysProject sysProject = sysProjectService.getProjectByUUID(projectHelpBo.getPUUID());
      if (ObjectUtils.isEmpty(sysProject)) {
        return SaResult.error("项目不存在");
      }
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
                    return !ObjectUtils.isEmpty(ph)
                        && ph.getCreateUserId().equals(sysUser.getUserId())
                        && ph.getPUUID().equals(projectHelpBo.getPUUID());
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
              .pUUID(sysProject.getUuid())
              .createUserId(sysUser.getUserId())
              .hpNumber(0)
              .pContent(sysProject.getContent())
              .pPrice(sysProject.getPrice())
              .pReleaseUserId(sysProject.getReleaseUserId())
              .hpStatus(0)
              .hpCreateNickName(sysUser.getNickName())
              .ipTerritory(sysUser.getIpTerritory())
              .projectCreateNickName(sysProject.getNickname())
              .projectId(sysProject.getPId())
              .build();
      // 创建项目助力信息，并同步到缓存
      int insertResult = projectHelpMapper.insert(projectHelp);
      if (insertResult == 0) {
        return SaResult.error("创建失败");
      } else {
        redisService.setCacheMapValue(
            RedisKey.PROJECT_HELP_LIST, projectHelp.getHpId(), projectHelp);
      }
      // 消息队列通知用户服务对该用户的创建次数减一
      sysUser.setCreateProjectHelpTimes(sysUser.getCreateProjectHelpTimes() - 1);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      return SaResult.ok().setData(projectHelp);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult updateProjectHelpNumber(ProjectHelpBo projectHelpBo) {

    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      ProjectHelp projectHelp = getProjectHelpByHpId(projectHelpBo.getHpId());
      if (ObjectUtils.isEmpty(projectHelp)) {
        return SaResult.error("助力链接不存在");
      }
      // 如果项目已经助力成功，那么直接返回好友已经成功
      if (projectHelp.getHpNumber() >= 10 || projectHelp.getHpStatus() == 1) {
        return SaResult.ok("助力成功");
      }
      if (projectHelp.getCreateUserId().equals(sysUser.getUserId())) {
        return SaResult.error("请分享给好友助力");
      }
      if (sysUser.getProjectHelpTimes() <= 0) {
        return SaResult.error("今日助力次数上限");
      }
      //  用户的助力次数减少一，返回助力成功
      sysUser.setProjectHelpTimes(sysUser.getProjectHelpTimes() - 1);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
      // 修改项目助力次数,并同步到缓存中
      projectHelp.setHpNumber(projectHelp.getHpNumber() + 1);
      int updateResult = this.updateProjectHelp(projectHelp);
      if (updateResult == 0) {
        log.error("项目助力失败,hpId:{}" + projectHelpBo.getHpId());
        return SaResult.error("服务器繁忙");
      }
      // 如果项目助力值大于等于10，邮件通知
      if (projectHelp.getHpNumber() >= 10) {
        SysUser createSysUser = this.getSysUserByUserId(projectHelp.getCreateUserId());
        ReceiveEmailBo receiveEmailBo =
            ReceiveEmailBo.builder()
                .receiverEmail(createSysUser.getEmail())
                .title("spaceObj")
                .content("项目：" + projectHelp.getPContent().substring(0, 10) + "... 助力成功！快去联系吧！")
                .build();
        kafkaSender.send(receiveEmailBo, KafKaTopics.HELP_PROJECT_SUCCESSFUL);
      }
      return SaResult.ok("助力成功");
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public int updateProjectHelp(ProjectHelp projectHelp) {
    QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("hp_id", projectHelp.getHpId());
    queryWrapper.eq("version", projectHelp.getVersion());
    projectHelp.setVersion(projectHelp.getVersion() + 1);
    int result = projectHelpMapper.update(projectHelp, queryWrapper);
    if (result == 0) {
      // 查询最新的数据，然后再次修改
      QueryWrapper<ProjectHelp> wrapper = new QueryWrapper<>();
      wrapper.eq("hp_id", projectHelp.getHpId());
      ProjectHelp projectHelpTwo = projectHelpMapper.selectById(wrapper);
      wrapper.eq("version", projectHelpTwo.getVersion());
      projectHelp.setVersion(projectHelpTwo.getVersion() + 1);
      result = this.projectHelpMapper.update(projectHelp, wrapper);
    }
    if (result == 0) {
      return result;
    }
    // 修改成功，刷新缓存信息
    redisService.setCacheMapValue(RedisKey.PROJECT_HELP_LIST, projectHelp.getHpId(), projectHelp);
    return result;
  }

  @Override
  public ProjectHelp getProjectHelpLink(String pUUID, String userId) {
    ProjectHelp projectHelp = null;
    try {
      List<ProjectHelp> projectHelpList = getProjectHelpList();
      List<ProjectHelp> resultProjectHelp = null;
      if (projectHelpList.size() == 0) {
        //  数据不存在
        return null;
      }
      resultProjectHelp =
          projectHelpList.stream()
              .filter(
                  hp -> {
                    return !ObjectUtils.isEmpty(hp)
                        && userId.equals(hp.getCreateUserId())
                        && pUUID.equals(hp.getPUUID());
                  })
              .collect(Collectors.toList());
      if (resultProjectHelp.size() == 0) {
        //  数据不存在
        return null;
      }
      projectHelp = resultProjectHelp.get(0);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      //  程序异常
      return null;
    }
    return projectHelp;
  }

  @Override
  public SaResult projectHelpList(ProjectHelpBo projectHelpBo) {
    List<ProjectHelp> list = null;
    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      List<ProjectHelp> listCaChe = getProjectHelpList();
      list =
          listCaChe.stream()
              .filter(
                  hp -> {
                    return !ObjectUtils.isEmpty(hp)
                        && hp.getCreateUserId().equals(sysUser.getUserId());
                  })
              .collect(Collectors.toList());
      // 实现分页查询
      // TODO
      int endNumber = 0;
      int startNumber = 0;
      startNumber = (projectHelpBo.getCurrentPage() - 1) * projectHelpBo.getPageSize();
      if (list.size() > projectHelpBo.getPageSize() * projectHelpBo.getCurrentPage()) {
        endNumber = projectHelpBo.getPageSize();
      } else {
        endNumber = list.size();
      }
      if (startNumber > list.size()) {
        list.clear();
        return SaResult.ok().setData(list);
      }
      list = list.subList(startNumber, endNumber);

      return SaResult.ok().setData(list);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public ProjectHelp getProjectHelpByHpId(String hpId) {
    boolean hasKey = redisService.HExists(RedisKey.PROJECT_HELP_LIST, hpId);
    if (hasKey) {
      return redisService.getCacheMapValue(RedisKey.PROJECT_HELP_LIST, hpId, ProjectHelp.class);
    } else {
      //  获取同步锁
      boolean flag = redissonService.tryLock(hpId);
      if (!flag) {
        return null;
      } else {
        // 获取成功，再次判断是否存在key
        hasKey = redisService.HExists(RedisKey.PROJECT_HELP_LIST, hpId);
        if (hasKey) {
          return redisService.getCacheMapValue(RedisKey.PROJECT_HELP_LIST, hpId, ProjectHelp.class);
        }
        QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper();
        queryWrapper.eq("hp_id", hpId);
        ProjectHelp projectHelp = projectHelpMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(projectHelp)) {
          redisService.setCacheMapValue(RedisKey.PROJECT_HELP_LIST, hpId, null);
        }
        redisService.setCacheMapValue(RedisKey.PROJECT_HELP_LIST, hpId, projectHelp);
        return projectHelp;
      }
    }
  }
}
