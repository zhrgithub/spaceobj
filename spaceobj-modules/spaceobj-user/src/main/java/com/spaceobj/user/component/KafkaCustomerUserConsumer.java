package com.spaceobj.user.component;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.spaceobj.common.core.utils.ExceptionUtil;
import com.spaceobj.common.kafka.constant.KafKaTopics;
import com.spaceobj.common.kafka.utils.KafkaSourceToTarget;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.CustomerUserService;
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

  @Autowired private CustomerUserService customerUserService;

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
                SysUser sysUser = KafkaSourceToTarget.getObject(message, SysUser.class);
                if (ObjectUtils.isNotEmpty(sysUser)) {
                  sysUser = customerUserService.getUserInfoByUserId(sysUser.getUserId());
                  sysUser.setInvitationValue(sysUser.getInvitationValue() + 1);
                  int result = customerUserService.updateUser(sysUser);
                  if (result == 0) {
                    LOG.error("user info update to mysql failed! sysUser {}" + sysUser.getUserId());
                    return;
                  }
                }
              } catch (Exception e) {
                ExceptionUtil.exceptionToString(e);
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
                SysUser sysUser = KafkaSourceToTarget.getObject(message, SysUser.class);
                int result = customerUserService.updateUser(sysUser);
                if (result == 0) {}
              } catch (Exception e) {
                ExceptionUtil.exceptionToString(e);
              }
            });
  }
}
