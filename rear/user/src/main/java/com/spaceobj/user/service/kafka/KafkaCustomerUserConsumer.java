package com.spaceobj.user.service.kafka;

import com.spaceobj.user.constent.KafKaTopics;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.utils.ConvertToTarget;
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

  private static final Logger LOG = LoggerFactory.getLogger(KafkaCustomerUserConsumer.class);

  /**
   * 用户注册
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.USER_REGISTER})
  public void userRegister(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              log.info("【+++++++++++++++++ record = {} 】", record);
              log.info("【+++++++++++++++++ message = {}】", message);

              try {
                SysUser sysUser = ConvertToTarget.getObject(message, SysUser.class);
                int result = sysUserMapper.insert(sysUser);
                if (result == 0) {
                  LOG.error("用户数据持久化新增失败!");
                }
              } catch (Exception e) {
                LOG.error("用户信息持久化新增失败! 失败信息：{}", e.getMessage());
              }
            });
  }
  /**
   * 用户数据更新
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.USER_UPDATE})
  public void userUpdate(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              log.info("【+++++++++++++++++ record = {} 】", record);
              log.info("【+++++++++++++++++ message = {}】", message);
              try {
                SysUser sysUser = ConvertToTarget.getObject(message, SysUser.class);
                int result = sysUserMapper.updateById(sysUser);
                if (result == 0) {
                  LOG.error("用户数据持久化修改失败!");
                }
              } catch (Exception e) {
                LOG.error("用户数据持久化修改失败!失败信息{}", e.getMessage());
              }
            });
  }
}
