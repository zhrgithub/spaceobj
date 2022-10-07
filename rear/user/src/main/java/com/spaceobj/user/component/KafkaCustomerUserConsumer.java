package com.spaceobj.user.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redis.common.service.RedisService;
import com.spaceobj.user.constant.KafKaTopics;
import com.spaceobj.user.constant.RedisKey;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.utils.KafKaSourceToTarget;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:30
 */
@Component
@Slf4j
public class KafkaCustomerUserConsumer {

  @Autowired private SysUserMapper sysUserMapper;

  @Autowired private RedisService redisService;

  private static final Logger LOG = LoggerFactory.getLogger(KafkaCustomerUserConsumer.class);

  /**
   * 邀请人的邀请值加一
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.INVITER_VALUE_ADD})
  public void inviterValueAdd(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                SysUser sysUser = KafKaSourceToTarget.getObject(message, SysUser.class);

                // 判断该用户是否存在
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", sysUser.getUserId());
                SysUser findUserById = sysUserMapper.selectOne(queryWrapper);
                if (ObjectUtils.isNotEmpty(findUserById)) {
                  int result = this.updateUser(findUserById);
                  if (result == 0) {
                    LOG.error("user info update to mysql failed! sysUser {}" + sysUser.getUserId());
                  }
                }

              } catch (Exception e) {
                LOG.error("update info update to mysql failed!failed info {}", e.getMessage());
              }
            });
  }

  /**
   * 用户数据更新
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.UPDATE_USER})
  public void userUpdate(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                SysUser sysUser = KafKaSourceToTarget.getObject(message, SysUser.class);
                int result = this.updateUser(sysUser);

                if (result == 0) {
                  LOG.error("user info update to mysql failed!");
                }
              } catch (Exception e) {
                LOG.error("update info update to mysql failed!failed info {}", e.getMessage());
              }
            });
  }

  /**
   * 修改用户
   *
   * @param sysUser
   */
  private int updateUser(SysUser sysUser) {

    QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("version", sysUser.getVersion());
    queryWrapper.eq("account", sysUser.getAccount());
    sysUser.setVersion(sysUser.getVersion() + 1);
    int result = sysUserMapper.update(sysUser, queryWrapper);
    if (result == 0) {
      // 查询最新的版本号然后更新,防止之前修改后的数据被覆盖
      QueryWrapper<SysUser> queryWrapper2 = new QueryWrapper<>();
      queryWrapper2.eq("account", sysUser.getAccount());
      sysUser = sysUserMapper.selectOne(queryWrapper2);
      return this.updateUser(sysUser);
    }
    redisService.setCacheMapValue(RedisKey.SYS_USER_LIST, sysUser.getAccount(), sysUser);
    return result;
  }
}
