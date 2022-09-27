package com.spaceobj.user.service.kafka;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spaceobj.domain.SysUser;
import com.spaceobj.user.constant.KafKaTopics;
import com.spaceobj.user.constant.RedisKey;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.utils.KafKaSourceToTarget;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 15:30
 */
@Component
@Slf4j
public class KafkaCustomerUserConsumer {

  @Autowired private SysUserMapper sysUserMapper;

  @Autowired private RedisTemplate redisTemplate;

  private static final Logger LOG = LoggerFactory.getLogger(KafkaCustomerUserConsumer.class);

  /**
   * 用户注册
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.ADD_USER})
  public void userRegister(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                SysUser sysUser = KafKaSourceToTarget.getObject(message, SysUser.class);
                int result = sysUserMapper.insert(sysUser);
                // 刷新缓存
                if (result == 1) {
                  this.updateRedis();
                }
                if (result == 0) {
                  LOG.error("user info save to mysql failed !");
                }
              } catch (Exception e) {
                LOG.error("user info save to mysql failed! fail info：{}", e.getMessage());
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
                int result = sysUserMapper.updateById(sysUser);

                // 刷新缓存
                if (result == 1) {
                  // 刷新Redis中的用户列表
                  this.updateRedis();
                }

                if (result == 0) {
                  LOG.error("user info update to mysql failed!");
                }
              } catch (Exception e) {
                LOG.error("update info update to mysql failed!failed info {}", e.getMessage());
              }
            });
  }

  /**
   * 刷新系统用户缓存信息
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.UPDATE_USER_LIST})
  public void userUpdateList(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                this.updateRedis();

              } catch (Exception e) {
                LOG.error("update info update to mysql failed!failed info {}", e.getMessage());
              }
            });
  }

  /** 刷新Redis缓存 */
  private void updateRedis() throws InterruptedException {
    if (getRedisSysUserListSyncStatus()) {
      Thread.sleep(50);
      this.updateRedis();
    } else {
      redisTemplate.opsForValue().set(RedisKey.SYS_USER_SYNC_STATUS, true);
      // 删除用户列表信息
      redisTemplate.delete(RedisKey.SYS_USER_LIST);
      // 查询用户列表信息
      List<SysUser> sysUserList;
      QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
      sysUserList = sysUserMapper.selectList(queryWrapper);
      // 更新用户列表信息
      redisTemplate.opsForList().rightPushAll(RedisKey.SYS_USER_LIST, sysUserList);
      redisTemplate.opsForValue().set(RedisKey.SYS_USER_SYNC_STATUS, false);
    }
  }

  /**
   * 获取用户同步的状态
   *
   * @return
   */
  public boolean getRedisSysUserListSyncStatus() {
    boolean hasKey = redisTemplate.hasKey(RedisKey.SYS_USER_SYNC_STATUS);
    if (!hasKey) {
      redisTemplate.opsForValue().set(RedisKey.SYS_USER_SYNC_STATUS, false);
      return false;
    } else {
      return (boolean) redisTemplate.opsForValue().get(RedisKey.SYS_USER_SYNC_STATUS);
    }
  }
}
