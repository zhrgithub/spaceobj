package com.spaceobj.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.common.service.RedisService;
import com.spaceobj.user.bo.ReceiveEmailBo;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.component.KafkaSender;
import com.spaceobj.user.constant.KafKaTopics;
import com.spaceobj.user.constant.RedisKey;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired private RedisService redisService;

  @Autowired private KafkaSender kafkaSender;

  private static final Logger LOG = LoggerFactory.getLogger(SysUserServiceImpl.class);

  @Override
  public SaResult findList(SysUserBo sysUserBo) {
    List<SysUser> list = null;
    try {
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper();

      if (StringUtils.isNotBlank(sysUserBo.getContent())) {
        queryWrapper
            .like("phone_number", sysUserBo.getContent())
            .or()
            .like("nick_name", sysUserBo.getContent())
            .or()
            .like("account", sysUserBo.getContent())
            .or()
            .like("username", sysUserBo.getContent())
            .or()
            .like("id_card_num", sysUserBo.getContent())
            .or()
            .like("email", sysUserBo.getContent());
      }
      System.out.println(sysUserBo.toString());
      Page<SysUser> page = new Page<>(sysUserBo.getCurrentPage(), sysUserBo.getPageSize());
      IPage<SysUser> iPage = sysUserMapper.selectPage(page, queryWrapper);
      list = iPage.getRecords();
      return SaResult.ok().setData(list);
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("system user find list failed ", e.getMessage());
      return SaResult.error("查询失败");
    }
  }

  @Override
  public SaResult updateSysUser(SysUserBo sysUserBo) {
    SysUser sysUser = new SysUser();
    try {
      BeanConvertToTargetUtils.copyNotNullProperties(sysUserBo, sysUser);
      if (sysUserBo.getDisableStatus() == 1) {
        StpUtil.kickout(sysUserBo.getAccount());
        StpUtil.disable(sysUserBo.getAccount(), -1);
      } else {
        StpUtil.untieDisable(sysUserBo.getAccount());
      }
      int result = sysUserMapper.updateById(sysUser);
      if (result == 0) {
        return SaResult.error("修改失败");
      }
      // 更新缓存
      redisService.setCacheMapValue(RedisKey.SYS_USER_LIST, sysUser.getAccount(), sysUser);
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("update sysUser failed", e.getMessage());
      return SaResult.error("用户更新失败");
    }
    return SaResult.ok("用户更新成功");
  }

  @Override
  public void updateAll(SysUserBo sysUserBo) {
    try {
      UpdateWrapper<SysUser> sysUserWrapper = new UpdateWrapper<>();
      SysUser sysUser = new SysUser();
      BeanConvertToTargetUtils.copyNotNullProperties(sysUserBo, sysUser);
      int result = sysUserMapper.update(sysUser, sysUserWrapper);
      if (result == 0) {
        LOG.error("logic update all system user failed");
      } else {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        List<SysUser> sysUserList = sysUserMapper.selectList(queryWrapper);
        for (SysUser obj : sysUserList) {
          // 更新缓存
          redisService.setCacheMapValue(RedisKey.SYS_USER_LIST, obj.getAccount(), obj);
        }
      }

    } catch (Exception e) {
      LOG.error("update all system user failed");
    }
  }

  @Override
  public void noticeAuditUserRealNameInfo() {
    QueryWrapper<SysUser> sysQueryWrapper = new QueryWrapper<>();
    sysQueryWrapper.eq("real_name_status", 2);
    List<SysUser> sysUserList = sysUserMapper.selectList(sysQueryWrapper);
    if (sysUserList.size() >= 0) {
      ReceiveEmailBo receiveEmail =
          ReceiveEmailBo.builder()
              .receiverEmail("zhr_java@163.com")
              .title("用户待审核通知")
              .content("尊敬的管理员您好:现在有需要您审核的实名认证用户，请您尽快处理！")
              .build();
      kafkaSender.send(receiveEmail, KafKaTopics.AUDIT_NOTICE);
    }
  }
}
