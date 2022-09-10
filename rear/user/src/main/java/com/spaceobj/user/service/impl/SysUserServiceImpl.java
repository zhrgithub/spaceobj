package com.spaceobj.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.domain.SysUser;
import com.spaceobj.user.bo.ReceiveEmailBo;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.constant.KafKaTopics;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.service.SysUserService;
import com.spaceobj.user.service.kafka.KafkaSender;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private KafkaSender kafkaSender;

  private static final Logger LOG = LoggerFactory.getLogger(SysUserServiceImpl.class);

  @Override
  public SaResult findList(SysUserBo sysUserBo) {
    List<SysUser> list = null;
    try {
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
      if (ObjectUtils.isNotEmpty(sysUserBo)) {
        if (StringUtils.isNotBlank(sysUserBo.getAccount())) {
          queryWrapper.like("account", sysUserBo.getAccount());
        }
        if (StringUtils.isNotBlank(sysUserBo.getPhoneNumber())) {
          queryWrapper.like("phone_number", sysUserBo.getPhoneNumber());
        }

        if (StringUtils.isNotBlank(sysUserBo.getUserType())) {
          queryWrapper.eq("user_type", sysUserBo.getUserType());
        }

        if (StringUtils.isNotBlank(sysUserBo.getUsername())) {
          queryWrapper.like("username", sysUserBo.getUsername());
        }

        if (StringUtils.isNotBlank(sysUserBo.getNickName())) {
          queryWrapper.like("nick_name", sysUserBo.getNickName());
        }

        if (sysUserBo.getOnlineStatus() != null) {
          queryWrapper.eq("online_status", sysUserBo.getOnlineStatus());
        }

        if (StringUtils.isNotBlank(sysUserBo.getIdCardNum())) {
          queryWrapper.eq("id_card_num", sysUserBo.getIdCardNum());
        }

        if (sysUserBo.getRealNameStatus() != null) {
          queryWrapper.eq("real_name_status", sysUserBo.getRealNameStatus());
        }

        if (sysUserBo.getDisableStatus() != null) {
          queryWrapper.eq("disable_status", sysUserBo.getDisableStatus());
        }
      }
      if (sysUserBo.getCurrentPage() == null || sysUserBo.getPageSize() == null) {
        sysUserBo.setCurrentPage(0);
        sysUserBo.setPageSize(10);
      }
      Page<SysUser> page = new Page<>(sysUserBo.getCurrentPage(), sysUserBo.getPageSize());
      IPage<SysUser> iPage = sysUserMapper.selectPage(page, queryWrapper);
      list = iPage.getRecords();
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("system user find list failed ", e.getMessage());
      return SaResult.error("查询失败");
    }
    return SaResult.ok().setData(list);
  }

  @Override
  public SaResult updateSysUser(SysUserBo sysUserBo) {
    SysUser sysUser = new SysUser();
    try {
      if (!redisTemplate.hasKey(sysUserBo.getAccount())) {
        return SaResult.error("账户不存在！");
      }

      BeanConvertToTargetUtils.copyNotNullProperties(sysUserBo, sysUser);
      if (sysUserBo.getDisableStatus() == 1) {
        StpUtil.kickout(sysUserBo.getAccount());
        StpUtil.disable(sysUserBo.getAccount(), -1);
      } else {
        StpUtil.untieDisable(sysUserBo.getAccount());
      }
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
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
      }
      if (result == 1) {
        kafkaSender.send(new Object(), KafKaTopics.UPDATE_USER_LIST);
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
