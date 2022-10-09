package com.spaceobj.projectHelp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.common.service.RedisService;
import com.redis.common.service.RedissonService;
import com.spaceobj.projectHelp.bo.ProjectHelpBo;
import com.spaceobj.projectHelp.bo.ReceiveEmailBo;
import com.spaceobj.projectHelp.component.ProjectClient;
import com.spaceobj.projectHelp.component.UserClient;
import com.spaceobj.projectHelp.pojo.SysProject;
import com.spaceobj.projectHelp.pojo.SysUser;
import com.spaceobj.projectHelp.constant.KafKaTopics;
import com.spaceobj.projectHelp.constant.RedisKey;
import com.spaceobj.projectHelp.mapper.ProjectHelpMapper;
import com.spaceobj.projectHelp.pojo.ProjectHelp;
import com.spaceobj.projectHelp.service.ProjectHelpService;
import com.spaceobj.projectHelp.component.KafkaSender;
import com.spaceobj.projectHelp.util.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.security.PublicKey;
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

  @Value("${publicKey}")
  private String publicKey;

  @Resource private UserClient userClient;

  @Resource private ProjectClient projectClient;

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
      Object res = userClient.getUserInfoByAccount(account).getData();
      sysUser = RsaUtils.decryptByPrivateKey(res, SysUser.class, privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return sysUser;
  }

  public SysUser getSysUserByUserId(String userId) {
    SysUser sysUser = null;
    try {
      Object res = userClient.getSysUserByUserId(userId).getData();
      sysUser = RsaUtils.decryptByPrivateKey(res, SysUser.class, privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return sysUser;
  }

  /**
   * 根据UUID获取项目列表信息
   *
   * @return
   */
  public SysProject getSysProject(String uuid) {
    SysProject sysProject = null;
    try {
      Object obj = projectClient.getEncryptProjectByUUID(uuid);
      sysProject = RsaUtils.decryptByPrivateKey(obj, SysProject.class, privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return sysProject;
  }

  /**
   * 同步数据到Redis缓存，并返回查询到的数据
   *
   * @return
   */
  private List<ProjectHelp> getProjectHelpList() {
    List<ProjectHelp> list = null;

    try {
      boolean hasKey = redisService.hasKey(RedisKey.PROJECT_HELP_LIST);
      if (hasKey) {
        list = redisService.getHashMapValues(RedisKey.PROJECT_HELP_LIST);
        return list;
      } else {
        boolean flag = redissonService.tryLock(RedisKey.PROJECT_HELP_LIST_SYNC_STATUS);
        if (!flag) {
          return null;
        } else {
          // 再次查询缓存，存在则返回
          hasKey = redisService.hasKey(RedisKey.PROJECT_HELP_LIST);
          if (hasKey) {
            list = redisService.getHashMapValues(RedisKey.PROJECT_HELP_LIST);
            return list;
          }
          QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper();
          queryWrapper.orderByDesc("create_time");
          list = projectHelpMapper.selectList(queryWrapper);
          // 缓存同步
          for (ProjectHelp p : list) {
            redisService.setCacheMapValue(RedisKey.PROJECT_HELP_LIST, p.getHpId(), p);
          }
          return list;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public SaResult createProjectHelpLink(ProjectHelpBo projectHelpBo) {

    try {
      String loginId = StpUtil.getLoginId().toString();
      SysUser sysUser = getSysUser(loginId);
      if (!Pattern.matches(RegexPool.EMAIL, sysUser.getEmail())) {
        return SaResult.error("请设置您的邮箱");
      }

      // 如果用户的创建剩余次数小于10次，提醒明天再来
      if (sysUser.getCreateProjectHelpTimes() <= 0) {
        return SaResult.error("今日分享链接创建已上限，明天再来吧！");
      }
      // 判断项目中是否有该项目的id
      // 根据前端传递过来的项目id，判断项目列表中是否有该项目
      SysProject sysProject = getSysProject(projectHelpBo.getPUUID());
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
                  ph ->
                      ph.getCreateUserId().equals(sysUser.getUserId())
                          && ph.getPUUID().equals(projectHelpBo.getPUUID()))
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
      e.printStackTrace();
      logger.error("project help failed", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  private int updateProjectHelp(ProjectHelp projectHelp) {
    QueryWrapper<ProjectHelp> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("hp_id", projectHelp.getHpId());
    queryWrapper.eq("version", projectHelp.getVersion());
    projectHelp.setVersion(projectHelp.getVersion() + 1);
    int result = projectHelpMapper.update(projectHelp, queryWrapper);
    if (result == 0) {
      // 查询最新的数据，然后再次修改
      QueryWrapper<ProjectHelp> wrapper = new QueryWrapper<>();
      wrapper.eq("hp_id", projectHelp.getHpId());
      projectHelp = projectHelpMapper.selectById(wrapper);
      queryWrapper.eq("version", projectHelp.getVersion());
      projectHelp.setVersion(projectHelp.getVersion() + 1);
      return this.projectHelpMapper.update(projectHelp, queryWrapper);
    }
    // 修改成功，刷新缓存信息
    redisService.setCacheMapValue(RedisKey.PROJECT_HELP_LIST, projectHelp.getHpId(), projectHelp);
    return result;
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
      // 实现分页查询
      // TODO
      return SaResult.ok().setData(resultList);
    } catch (Exception e) {
      logger.error("get project help list failed", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public ProjectHelp getProjectHelpByHpId(String hpId) {
    boolean hasKey = redisService.HExists(RedisKey.PROJECT_HELP_LIST, hpId);
    if (hasKey) {
      return redisService.getCacheMapValue(RedisKey.PROJECT_HELP_LIST, hpId);
    } else {
      //  获取同步锁
      boolean flag = redissonService.tryLock(hpId);
      if (!flag) {
        return null;
      } else {
        // 获取成功，再次判断是否存在key
        hasKey = redisService.HExists(RedisKey.PROJECT_HELP_LIST, hpId);
        if (hasKey) {
          return redisService.getCacheMapValue(RedisKey.PROJECT_HELP_LIST, hpId);
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
