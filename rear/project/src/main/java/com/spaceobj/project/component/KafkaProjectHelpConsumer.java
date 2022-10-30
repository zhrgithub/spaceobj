package com.spaceobj.project.component;

import com.redis.common.service.RedisService;
import com.spaceobj.project.constant.KafKaTopics;
import com.spaceobj.project.constant.RedisKey;
import com.spaceobj.project.mapper.ProjectHelpMapper;
import com.spaceobj.project.pojo.ProjectHelp;
import com.spaceobj.project.service.ProjectHelpService;
import com.spaceobj.project.util.KafkaSourceToTarget;
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
public class KafkaProjectHelpConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(KafkaProjectHelpConsumer.class);

  @Autowired private ProjectHelpService projectHelpService;

  @Autowired private ProjectHelpMapper projectHelpMapper;

  @Autowired private RedisService redisService;

  /**
   * 监听项目助力更新,一般来自项目表中的通知
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.ADD_HELP_PROJECT})
  public void addHelpProject(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                ProjectHelp projectHelp = KafkaSourceToTarget.getObject(message, ProjectHelp.class);
                // 创建项目助力信息，并同步到缓存
                int insertResult = projectHelpMapper.insert(projectHelp);
                if (insertResult == 0) {
                  LOG.error("project help info update to mysql failed !");
                } else {
                  redisService.setCacheMapValue(
                      RedisKey.PROJECT_HELP_LIST, projectHelp.getHpId(), projectHelp);
                }
              } catch (Exception e) {
                LOG.error("project help info update to mysql failed !fail info {}", e.getMessage());
              }
            });
  }

  /**
   * 监听项目助力更新,一般来自项目表中的通知
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.UPDATE_HELP_PROJECT})
  public void updateProjectHelp(ConsumerRecord<?, ?> record) {
    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                ProjectHelp projectHelp = KafkaSourceToTarget.getObject(message, ProjectHelp.class);
                int result = projectHelpService.updateProjectHelp(projectHelp);
                if (result == 0) {
                  LOG.error("project help info update to mysql failed !");
                }
              } catch (Exception e) {
                LOG.error("project help info update to mysql failed !fail info {}", e.getMessage());
              }
            });
  }
}
