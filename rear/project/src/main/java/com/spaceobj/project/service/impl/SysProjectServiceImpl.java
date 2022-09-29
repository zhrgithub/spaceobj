package com.spaceobj.project.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.concurrent.TimeUnit;
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

  @Autowired private SysProjectMapper sysProjectMapper;

  Logger LOG = LoggerFactory.getLogger(SysProjectServiceImpl.class);

  public boolean getRedisProjectSyncStatus() {
    boolean hasKey = redisTemplate.hasKey(RedisKey.REDIS_PROJECT_SYNC_STATUS);
    if (!hasKey) {
      redisTemplate.opsForValue().set(RedisKey.REDIS_PROJECT_SYNC_STATUS, false);
      return false;
    } else {
      return (boolean) redisTemplate.opsForValue().get(RedisKey.REDIS_PROJECT_SYNC_STATUS);
    }
  }

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
      if (sysUser.getReleaseProjectTimes() <= 0) {
        return SaResult.error("今天发布次数已上线");
      }
      // 修改用户信息
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
      // 删除缓存
      redisTemplate.delete(RedisKey.PROJECT_LIST);
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
        SysProject checkProject = null;
        // 从缓存中获取数据
        List<SysProject> cacheProject = getSysProjectList();
        // 如果当前项目是在审核中那么返回不可重复修改
        List<SysProject> checkCacheProjectList =
            cacheProject.stream()
                .filter(
                    p -> {
                      return p.getUuid().equals(sysProject.getUuid());
                    })
                .collect(Collectors.toList());
        if (checkCacheProjectList.size() == 0) {
          // 缓存中不存在，查询MySQL中的数据，根据UUID查询项目
          List<SysProject> sysProjectList = null;
          QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
          queryWrapper.eq("p_uuid", sysProject.getUuid());
          sysProjectList = sysProjectMapper.selectList(queryWrapper);
          if (sysProjectList.size() == 0) {
            return SaResult.error("项目不存在");
          } else {
            checkProject = sysProjectList.get(0);
          }
        } else {
          checkProject = checkCacheProjectList.get(0);
        }
        if (checkProject.getStatus() == 0) {
          return SaResult.error("审核中");
        }
        String loginId = StpUtil.getLoginId().toString();
        SysUser sysUser = getSysUser(loginId);
        if (!sysUser.getUserId().equals(checkProject.getReleaseUserId())) {
          return SaResult.error("违规操作");
        }
        // 修改的项目设置成待审核
        sysProject.setStatus(0);
        // 根据项目id和version修改数据,每次修改都让版本号+1,用于处理并发修改业务
        int result = this.updateResult(sysProject);
        if (result == 0) {
          //  如果修改失败，那么根据id查询最新的版本号再次修改
          SysProject sysProjectById = sysProjectMapper.selectById(sysProject.getPId());
          sysProjectById.setStatus(0);
          sysProjectById.setContent(sysProject.getContent());
          sysProjectById.setPrice(sysProject.getPrice());
          sysProjectById.setIpAddress(sysProject.getIpAddress());
          return this.updateProject(sysProjectById);
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
      int result = this.updateResult(project);
      if (result == 0) {
        return SaResult.error("审核失败");
      }
      return SaResult.ok("审核成功");
    } catch (Exception e) {
      e.printStackTrace();
      return SaResult.error("审核失败");
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
                (projectSearchBo.getPageNumber() - 1) * projectSearchBo.getPageSize(), pageSiz);
      } else if (projectSearchBo.getProjectType() == 1) {
        // 查询自己发布的信息,根据项目创建人id查询项目
        QueryWrapper<SysProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p_release_user_id", projectSearchBo.getUserId());
        Page<SysProject> page =
            new Page<>(projectSearchBo.getPageNumber(), projectSearchBo.getPageSize());
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
   * 同步数据到Redis缓存，并返回查询到的数据
   *
   * @return
   */
  private List<SysProject> getSysProjectList() throws InterruptedException {
    List<SysProject> list = null;
    try {
      boolean flag = redisTemplate.hasKey(RedisKey.PROJECT_LIST);
      if (!flag) {
        if (this.getRedisProjectSyncStatus()) {
          Thread.sleep(50);
          return this.getSysProjectList();
        } else {
          redisTemplate.opsForValue().set(RedisKey.REDIS_PROJECT_SYNC_STATUS, true);
          redisTemplate.delete(RedisKey.PROJECT_LIST);
          QueryWrapper<SysProject> queryWrapper = new QueryWrapper();
          queryWrapper.orderByDesc("create_time");
          list = sysProjectMapper.selectList(queryWrapper);
          redisTemplate.opsForList().rightPushAll(RedisKey.PROJECT_LIST, list.toArray());
          // 设置过期时间
          redisTemplate.expire(
              RedisKey.PROJECT_LIST, RedisKey.PROJECT_LIST_EXPIRE_TIME, TimeUnit.MINUTES);
          redisTemplate.opsForValue().set(RedisKey.REDIS_PROJECT_SYNC_STATUS, false);
          return list;
        }
      } else {
        list = redisTemplate.opsForList().range(RedisKey.PROJECT_LIST, 0, -1);
        return list;
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
      if (StringUtils.isNotBlank(projectSearchBo.getContent())) {
        queryWrapper.like("p_content", projectSearchBo.getContent());
      }
      if (ObjectUtils.isNotEmpty(projectSearchBo.getPId())) {
        queryWrapper.like("p_id", projectSearchBo.getPId());
      }
      Page<SysProject> page = new Page<>();
      IPage<SysProject> iPage = sysProjectMapper.selectPage(page, queryWrapper);
      list = iPage.getRecords();
      return SaResult.ok().setData(list);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return SaResult.error("服务器异常");
    }
  }

  @Override
  public void addPageViews(long projectId) {
    try {

      // 只有发布成功，并且同步到缓存中的项目才可以追加浏览次数，从Redis中先判断是否存在，不存在的话，停止执行
      List<SysProject> sysProjectList = getSysProjectList();
      List<SysProject> resultSysProject =
          sysProjectList.stream().filter(p -> p.getPId() == projectId).collect(Collectors.toList());
      if (resultSysProject.size() == 0) {
        return;
      }
      // 根据id查询项目
      SysProject sysProject = sysProjectMapper.selectById(projectId);
      int result = this.updateResult(sysProject);
      if (result == 0) {
        this.addPageViews(projectId);
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
      sysProject.setVersion(sysProject.getVersion() - 1);
    }
    return result;
  }

  @Override
  public SaResult getPhoneNumberByProjectId(GetPhoneNumberBo getPhoneNumberBo) {
    try {
      String loginId = (String) StpUtil.getLoginId();
      SysUser sysUser = getSysUser(loginId);
      getPhoneNumberBo.setUserId(sysUser.getUserId());
      List<SysProject> list = getSysProjectList();
      List<SysProject> sysProjectList = null;
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
        if (helpBo.getHpStatus() == 1 || helpBo.getHpNumber() >= 10) {
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
      return SaResult.error("获取联系方式失败，服务器异常");
    }
  }

  /**
   * 获取待审核项目的列表
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

  /**
   * 根据账户获取用户信息
   *
   * @param account
   * @return
   * @throws InterruptedException
   */
  public SysUser getSysUser(String account) throws InterruptedException {
    List<SysUser> sysUserList = null;
    SysUser sysUser = null;
    try {
      boolean flag = redisTemplate.hasKey(RedisKey.SYS_USER_LIST);
      if (!flag) {
        // 刷新用户缓存信息
        kafkaSender.send(new Object(), KafKaTopics.UPDATE_USER_LIST);
        Thread.sleep(200);
        return this.getSysUser(account);
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
        return sysUser;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
