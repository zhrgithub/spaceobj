package com.spaceobj.project.service.kafka;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spaceobj.project.constant.KafKaTopics;
import com.spaceobj.project.constant.RedisKey;
import com.spaceobj.project.mapper.SysProjectMapper;
import com.spaceobj.domain.SysProject;
import com.spaceobj.project.util.KafkaSourceToTarget;
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
public class KafkaUserConsumer {

  @Autowired private SysProjectMapper sysProjectMapper;

  @Autowired private RedisTemplate redisTemplate;

  private static final Logger LOG = LoggerFactory.getLogger(KafkaUserConsumer.class);

  @KafkaListener(topics = {KafKaTopics.ADD_PROJECT})
  public void addProject(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                SysProject sysProject = KafkaSourceToTarget.getObject(message, SysProject.class);
                int result = sysProjectMapper.insert(sysProject);
                // 刷新缓存
                if (result == 1) {
                  this.updateRedis();
                }
                if (result == 0) {
                  LOG.error("project info save to mysql failed !");
                }
              } catch (Exception e) {
                LOG.error("project info save to mysql failed! fail info：{}", e.getMessage());
              }
            });
  }

  @KafkaListener(topics = {KafKaTopics.UPDATE_PROJECT})
  public void updateProject(ConsumerRecord<?, ?> record) {

    Optional.ofNullable(record.value())
        .ifPresent(
            message -> {
              try {
                SysProject sysProject = KafkaSourceToTarget.getObject(message, SysProject.class);
                System.out.println(sysProject);
                int result = sysProjectMapper.updateById(sysProject);
                // 刷新缓存
                if (result == 1) {
                  this.updateRedis();
                }
                if (result == 0) {
                  LOG.error("project info update to mysql failed !");
                }
              } catch (Exception e) {
                LOG.error("project info update to mysql failed! fail info：{}", e.getMessage());
              }
            });
  }

  /** 刷新Redis缓存 */
  private void updateRedis() {

    redisTemplate.delete(RedisKey.PROJECT_LIST);
    List<SysProject> sysProjectList;
    QueryWrapper<SysProject> queryWrapper = new QueryWrapper();
    sysProjectList = sysProjectMapper.selectList(queryWrapper);
    redisTemplate.opsForList().rightPushAll(RedisKey.PROJECT_LIST, sysProjectList.toArray());
  }
}
