package com.spaceobj.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.constent.KafKaTopics;
import com.spaceobj.user.constent.RedisKey;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
import com.spaceobj.user.service.kafka.KafkaSender;
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
  public SaResult findList(String searchValue) {
    List<SysUser> list = null;
    try {
      Long size = redisTemplate.opsForList().size(RedisKey.SYS_USER_LIST);
      if (size == 0) {
        QueryWrapper queryWrapper = new QueryWrapper();
        list = sysUserMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(RedisKey.SYS_USER_LIST, list.toArray());
      } else {
        // 此处建议使用pipeLine来提升性能
        list = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
      return SaResult.error(e.getMessage());
    }
    return SaResult.ok().setData(list);
  }

  @Override
  public SaResult updateSysUser(SysUserBo sysUserBo) {
    Integer result = -1;
    SysUser sysUser =
        SysUser.builder()
            .userId(sysUserBo.getUserId())
            .inviteUserId(sysUserBo.getInviteUserId())
            .account(sysUserBo.getAccount())
            .emailCode(sysUserBo.getEmailCode())
            .password(sysUserBo.getPassword())
            .token(sysUserBo.getToken())
            .openId(sysUserBo.getOpenId())
            .phoneNumber(sysUserBo.getPhoneNumber())
            .assistValue(sysUserBo.getAssistValue())
            .invitationValue(sysUserBo.getInvitationValue())
            .userType(sysUserBo.getUserType())
            .userRights(sysUserBo.getUserRights())
            .username(sysUserBo.getUsername())
            .nickName(sysUserBo.getNickName())
            .photoUrl(sysUserBo.getPhotoUrl())
            .onlineStatus(sysUserBo.getOnlineStatus())
            .userInfoEditStatus(sysUserBo.getUserInfoEditStatus())
            .idCardNum(sysUserBo.getIdCardNum())
            .idCardPic(sysUserBo.getIdCardPic())
            .realNameStatus(sysUserBo.getRealNameStatus())
            .ip(sysUserBo.getIp())
            .ipTerritory(sysUserBo.getIpTerritory())
            .editInfoTimes(sysUserBo.getEditInfoTimes())
            .sendCodeTimes(sysUserBo.getSendCodeTimes())
            .releaseProjectTimes(sysUserBo.getReleaseProjectTimes())
            .projectHelpTimes(sysUserBo.getProjectHelpTimes())
            .deviceType(sysUserBo.getDeviceType())
            .createProjectHelpTimes(sysUserBo.getCreateProjectHelpTimes())
            .disableStatus(sysUserBo.getDisableStatus())
            .build();
    try {

      if (sysUserBo.getDisableStatus() == 0) {
        StpUtil.kickout(sysUserBo.getAccount());
        StpUtil.disable(sysUserBo.getAccount(), -1);
      } else {
        StpUtil.untieDisable(sysUserBo.getAccount());
      }
      kafkaSender.send(sysUser, KafKaTopics.UPDATE_USER);
    } catch (RuntimeException e) {
      LOG.error("update sysUser failed", e.getMessage());
      return SaResult.error(e.getMessage()).setData(result);
    }

    return SaResult.ok().setData(result);
  }
}
