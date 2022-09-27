package com.spaceobj.projectHelp.service.kafka;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spaceobj.projectHelp.constant.KafKaTopics;
import com.spaceobj.projectHelp.constant.RedisKey;
import com.spaceobj.projectHelp.mapper.ProjectHelpMapper;
import com.spaceobj.domain.ProjectHelp;
import com.spaceobj.projectHelp.util.KafkaSourceToTarget;
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
public class KafkaProjectHelpConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(KafkaProjectHelpConsumer.class);

  @Autowired private ProjectHelpMapper projectHelpMapper;

  @Autowired private RedisTemplate redisTemplate;

  /**
   * 监听项目助力新增
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
                int result = projectHelpMapper.insert(projectHelp);
                if (result == 0) {
                  LOG.error("project help info save to mysql failed !");
                }
                // 新增成功，删除缓存
                if (result == 1) {
                  redisTemplate.delete(RedisKey.PROJECT_HELP_LIST);
                }
              } catch (Exception e) {
                LOG.error("project help info save to mysql failed !fail info {}", e.getMessage());
              }
            });
  }

  /**
   * 监听项目助力更新
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
                int result = projectHelpMapper.updateById(projectHelp);
                if (result == 0) {
                  LOG.error("project help info update to mysql failed !");
                }
                // 更新成功，那么刷新缓存
                if (result == 1) {
                  redisTemplate.delete(RedisKey.PROJECT_HELP_LIST);
                }
              } catch (Exception e) {
                LOG.error("project help info update to mysql failed !fail info {}", e.getMessage());
              }
            });
  }

  /**
   * 项目助力列表缓存更新
   *
   * @param record
   */
  @KafkaListener(topics = {KafKaTopics.UPDATE_HELP_PROJECT_LIST})
  public void updateProjectHelpList(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                redisTemplate.delete(RedisKey.PROJECT_HELP_LIST);
              } catch (Exception e) {
                LOG.error("project help info update to mysql failed !fail info {}", e.getMessage());
              }
            });
  }
}
