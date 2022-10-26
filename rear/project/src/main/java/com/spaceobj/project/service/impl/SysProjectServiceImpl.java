package com.spaceobj.project.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.common.service.RedisService;
import com.redis.common.service.RedissonService;
import com.spaceobj.project.bo.GetPhoneNumberBo;
import com.spaceobj.project.bo.ProjectSearchBo;
import com.spaceobj.project.component.KafkaSender;
import com.spaceobj.project.component.UserClient;
import com.spaceobj.project.constant.KafKaTopics;
import com.spaceobj.project.constant.RedisKey;
import com.spaceobj.project.mapper.SysProjectMapper;
import com.spaceobj.project.pojo.ProjectHelp;
import com.spaceobj.project.pojo.SysProject;
import com.spaceobj.project.pojo.SysUser;
import com.spaceobj.project.service.SysProjectService;
import com.spaceobj.project.util.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysProjectServiceImpl extends ServiceImpl<SysProjectMapper, SysProject>
    implements SysProjectService {

  @Autowired private RedisService redisService;
  @Autowired private RedissonService redissonService;

  @Autowired private KafkaSender kafkaSender;

  @Autowired private SysProjectMapper sysProjectMapper;

  @Resource private UserClient userClient;

  @Value("${privateKey}")
  private String privateKey;

  @Value("${publicKey}")
  private String publicKey;

  Logger LOG = LoggerFactory.getLogger(SysProjectServiceImpl.class);

  @Override
  public SaResult addProject(SysProject sysProject) {

    try {
      // 校验内容是否重复
      List<SysProject> sysProjectList = getSysProjectList();
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
      String loginId = (String) StpUtil.getLoginId();
      SysUser sysUser = getSysUser(loginId);
      if (StringUtils.isEmpty(sysUser.getEmail())
          || StringUtils.isEmpty(sysUser.getPhoneNumber())) {
        return SaResult.error("请设置邮箱和手机号");
      }
      if (sysUser.getReleaseProjectTimes() <= 0) {
        return SaResult.error("今天发布次数已上线");
      }
      // 将用户的的发布次数减少一，修改用户信息
      sysUser.setReleaseProjectTimes(sysUser.getReleaseProjectTimes() - 1);
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);

      // 生成UUID
      String uuid = UUID.randomUUID().toString();
      sysProject.setUuid(uuid);
      // 设置成审核中
      sysProject.setStatus(0);
      sysProject.setReleaseUserId(sysUser.getUserId());
      int result = sysProjectMapper.insert(sysProject);
      if (result == 0) {
        return SaResult.error("新增失败");
      }
      // 刷新缓存
      redisService.setCacheMapValue(RedisKey.PROJECT_LIST, uuid, sysProject);
      return SaResult.ok();
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("project add error", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult updateProject(SysProject sysProject) {
    try {
      if (ObjectUtils.isNotNull(sysProject.getUuid())) {
        // 校验是否存在该项目，如果存在该项目，给checkCacheProject赋值
        SysProject project = this.getProjectByUUID(sysProject.getUuid());
        // 如果当前项目是在审核中那么返回不可重复修改
        if (ObjectUtils.isEmpty(project)) {
          return SaResult.error("项目不存在");
        }
        if (project.getStatus() == 0) {
          return SaResult.error("审核中");
        }
        String loginId = StpUtil.getLoginId().toString();
        SysUser sysUser = getSysUser(loginId);
        if (!sysUser.getUserId().equals(project.getReleaseUserId())) {
          return SaResult.error("违规操作");
        }
        // 修改的项目设置成待审核
        sysProject.setStatus(0);
        // 根据项目id和version修改数据,每次修改都让版本号+1,用于处理并发修改业务
        int result = this.updateResult(sysProject);
        if (result == 0) {
          LOG.error("项目更新失败{}" + sysProject.getUuid());
          //  如果修改失败，那么根据id查询最新的版本号再次修改
          return SaResult.error("修改失败");
        }
        return SaResult.ok();
      }
      return SaResult.error("请求错误");
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("update project exception", e.getMessage());
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public SaResult auditProject(SysProject project) {
    try {
      QueryWrapper<SysProject> wrapper = new QueryWrapper<>();
      wrapper.eq("p_id", project.getPId());
      SysProject sysProject = sysProjectMapper.selectOne(wrapper);
      sysProject.setStatus(project.getStatus());
      sysProject.setMessage(project.getMessage());
      int result = this.updateResult(sysProject);
      if (result == 0) {
        LOG.error("项目审核失败{}" + project.getUuid());
        return SaResult.error("审核失败");
      }
      return SaResult.ok("审核成功");
    } catch (Exception e) {
      e.printStackTrace();
      return SaResult.error("审核失败,服务器异常");
    }
  }

  @Override
  public SaResult findList(ProjectSearchBo projectSearchBo) {
    try {
      if (projectSearchBo.getProjectType() == 1) {
        String loginId = StpUtil.getLoginId().toString();
        SysUser sysUser = getSysUser(loginId);
        projectSearchBo.setUserId(sysUser.getUserId());
      }
      List<SysProject> list = getSysProjectList();
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
        int pageSiz = 0;
        if (list.size() > projectSearchBo.getPageSize()) {
          pageSiz = projectSearchBo.getPageSize();
        } else {
          pageSiz = list.size();
        }
        list =
            list.subList(
                (projectSearchBo.getCurrentPage() - 1) * projectSearchBo.getPageSize(), pageSiz);
      } else if (projectSearchBo.getProjectType() == 1) {
        // 查询自己发布的信息,根据项目创建人id查询项目
        QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p_release_user_id", projectSearchBo.getUserId());
        Page<SysProject> page =
            new Page<>(projectSearchBo.getCurrentPage(), projectSearchBo.getPageSize());
        IPage<SysProject> iPage = sysProjectMapper.selectPage(page, queryWrapper);
        list = iPage.getRecords();
      } else {
        return SaResult.error("请求参数错误");
      }

      return SaResult.ok().setData(list);
    } catch (NotLoginException e) {
      e.printStackTrace();
      return SaResult.error(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return SaResult.error("项目列表查询结果异常");
    }
  }

  /**
   * 查询项目列表数据，先判断是否存在该key,不存在那么设置分布式锁，然后从MySQL中查询，然后同步到Redis中，返回数据
   *
   * @return
   */
  private List<SysProject> getSysProjectList() throws InterruptedException {
    List<SysProject> list = null;
    try {
      boolean hasKey = redisService.hasKey(RedisKey.PROJECT_LIST);
      if (hasKey) {
        list = redisService.getHashMapValues(RedisKey.PROJECT_LIST, SysProject.class);
        return list;
      } else {
        boolean flag = redissonService.tryLock(RedisKey.REDIS_PROJECT_SYNC_STATUS);
        if (!flag) {
          return null;
        } else {
          // 再次判断是否存在该key
          hasKey = redisService.hasKey(RedisKey.PROJECT_LIST);
          if (hasKey) {
            list = redisService.getHashMapValues(RedisKey.PROJECT_LIST, SysProject.class);
            return list;
          }
          QueryWrapper<SysProject> queryWrapper = new QueryWrapper();
          queryWrapper.orderByDesc("create_time");
          list = sysProjectMapper.selectList(queryWrapper);
          for (SysProject sysProject : list) {
            redisService.setCacheMapValue(RedisKey.PROJECT_LIST, sysProject.getUuid(), sysProject);
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
  public SaResult queryListAdmin(ProjectSearchBo projectSearchBo) {
    try {
      List<SysProject> list;
      QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
      if (!StringUtils.isEmpty(projectSearchBo.getContent())) {
        queryWrapper.like("p_content", projectSearchBo.getContent());
        queryWrapper.or().like("p_id", projectSearchBo.getContent());
      }
      System.out.println(projectSearchBo.getCurrentPage() + ";;;" + projectSearchBo.getPageSize());
      Page<SysProject> page =
          new Page<>(projectSearchBo.getCurrentPage(), projectSearchBo.getPageSize());
      IPage<SysProject> iPage = sysProjectMapper.selectPage(page, queryWrapper);
      list = iPage.getRecords();
      return SaResult.ok().setData(list);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public void addPageViews(String uuid) {
    try {

      // 只有发布成功，并且同步到缓存中的项目才可以追加浏览次数，从Redis中先判断是否存在，不存在的话，停止执行
      // 根据id查询项目
      SysProject sysProject = this.getProjectByUUID(uuid);
      int result = this.updateResult(sysProject);
      if (result == 0) {
        LOG.error("项目UUID：{}" + sysProject.getUuid() + "浏览次数添加失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("add Page view error", e.getMessage());
    }
  }

  /**
   * 根据项目id和version更新项目
   *
   * @param sysProject
   * @return
   */
  public int updateResult(SysProject sysProject) {
    QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("p_id", sysProject.getPId());
    queryWrapper.eq("version", sysProject.getVersion());
    sysProject.setVersion(sysProject.getVersion() + 1);
    int result = sysProjectMapper.update(sysProject, queryWrapper);
    if (result == 0) {
      // 查询最新的数据，然后再次修改
      QueryWrapper<SysProject> wrapper = new QueryWrapper<>();
      wrapper.eq("p_id", sysProject.getPId());
      System.out.println("sysproject: " + sysProject);
      sysProject = sysProjectMapper.selectOne(wrapper);
      wrapper.eq("version", sysProject.getVersion());
      sysProject.setVersion(sysProject.getVersion() + 1);
      result = sysProjectMapper.update(sysProject, wrapper);
    }
    // 修改成功，刷新缓存信息
    redisService.setCacheMapValue(RedisKey.PROJECT_LIST, sysProject.getUuid(), sysProject);
    return result;
  }

  @Override
  public SaResult getPhoneNumberByProjectId(GetPhoneNumberBo getPhoneNumberBo) {
    try {
      String loginId = (String) StpUtil.getLoginId();
      SysUser sysUser = getSysUser(loginId);
      getPhoneNumberBo.setUserId(sysUser.getUserId());
      SysProject sysProject = this.getProjectByUUID(getPhoneNumberBo.getPUUID());
      if (ObjectUtils.isEmpty(sysProject)) {
        return SaResult.error("项目不存在");
      }
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
        return SaResult.error("项目未通过审核");
      }
      // 判断该用户的助力列表中是否有该项目数据，当前方案要保证项目助力hash表的RedisKey永不失效
      // 判断是否已经获取到该项目联系人
      List<ProjectHelp> projectHelpList =
          redisService.getHashMapValues(RedisKey.PROJECT_HELP_LIST, ProjectHelp.class);
      List<ProjectHelp> resultProjectHelp = null;
      if (ObjectUtils.isEmpty(projectHelpList)) {
        return SaResult.error("服务器繁忙");
      }
      resultProjectHelp =
          projectHelpList.stream()
              .filter(
                  hp ->
                      hp.getCreateUserId().equals(getPhoneNumberBo.getUserId())
                          && getPhoneNumberBo.getPUUID().equals(hp.getPUUID()))
              .collect(Collectors.toList());
      if (resultProjectHelp.size() > 0) {
        ProjectHelp helpBo = resultProjectHelp.get(0);
        if (helpBo.getHpStatus() == 1 || helpBo.getHpNumber() >= 10) {
          // 获取项目发布者id的联系方式,后期此处修改成根据账户获取用户信息，项目中的userId设置成email,数据进行脱敏
          String releaseId = sysProject.getReleaseUserId();
          SysUser releaseProjectUser = this.getSysUserByUserId(releaseId);
          return SaResult.ok().setData(releaseProjectUser.getPhoneNumber());
        }
      }

      // 判断用户的邀请值大于0
      if (sysUser.getInvitationValue() > 0) {
        //  邀请值减一，如果项目助力列表中没有该项目，那么设置成已经获取到，如果没有，那么新增到项目助力列表并设置成已经获取到的状态
        sysUser.setInvitationValue(sysUser.getInvitationValue() - 1);
        ProjectHelp helpBo;
        if (!ObjectUtils.isEmpty(resultProjectHelp) && resultProjectHelp.size() > 0) {
          helpBo = resultProjectHelp.get(0);
          helpBo.setHpNumber(10);
          helpBo.setHpStatus(1);
          // 通知项目助力服务更新数据
          kafkaSender.send(helpBo, KafKaTopics.UPDATE_HELP_PROJECT);
        } else {
          helpBo =
              ProjectHelp.builder()
                  .hpId(UUID.randomUUID().toString())
                  .pUUID(sysProject.getUuid())
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
        String releaseId = sysProject.getReleaseUserId();
        SysUser releaseProjectUser = this.getSysUserByUserId(releaseId);
        return SaResult.ok().setData(releaseProjectUser.getPhoneNumber());
      }
      return SaResult.error("分享项目助力链接");
    } catch (Exception e) {
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  /**
   * 获取待审核项目的数量
   *
   * @return
   */
  @Override
  public SaResult getPendingReview() {
    QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("p_status", 0);
    List<SysProject> sysProjectList = sysProjectMapper.selectList(queryWrapper);
    return SaResult.ok().setData(sysProjectList.size());
  }

  @Override
  public SysProject getProjectByUUID(String uuid) {
    boolean hExists = redisService.HExists(RedisKey.PROJECT_LIST, uuid);
    if (hExists) {
      return redisService.getCacheMapValue(RedisKey.PROJECT_LIST, uuid, SysProject.class);
    } else {
      // 设置分布式锁
      boolean flag = redissonService.tryLock(uuid);
      if (!flag) {
        // 返回服务器繁忙
        return null;
      } else {
        //  成功获取到锁，那么再次判断是否已经存在这个hashKey，存在就返回，不存在就查询MySQL，然后同步到缓存中
        hExists = redisService.HExists(RedisKey.PROJECT_LIST, uuid);
        if (hExists) {
          return redisService.getCacheMapValue(RedisKey.PROJECT_LIST, uuid, SysProject.class);
        }
        QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p_uuid", uuid);
        SysProject sysProject = sysProjectMapper.selectOne(queryWrapper);
        // 如果MySQL中也没有查到那么设置成null
        if (ObjectUtils.isEmpty(sysProject)) {
          redisService.setCacheMapValue(RedisKey.PROJECT_LIST, sysProject.getUuid(), null);
        }
        // 同步到缓存中,并返回结果
        redisService.setCacheMapValue(RedisKey.PROJECT_LIST, sysProject.getUuid(), sysProject);
        return sysProject;
      }
    }
  }

  @Override
  public SaResult getEncryptProjectByUUID(String uuid) {
    SysProject sysProject = this.getProjectByUUID(uuid);
    byte[] res = RsaUtils.encryptByPublicKey(sysProject, publicKey);
    return SaResult.ok().setData(res);
  }

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
}
