package com.spaceobj.project.component;

import com.spaceobj.project.bo.ReceiveEmailBo;
import com.spaceobj.project.constant.KafKaTopics;
import com.spaceobj.project.service.SysProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务处理
 *
 * @author zhr_java@163.com
 * @date 2022/9/7 01:02
 */
@Slf4j
@Component("taskJob")
public class TaskJob {

  @Autowired private SysProjectService sysProjectService;

  @Autowired private KafkaSender kafkaSender;

  /** 朝九晚五，每一个小时判断一下是否需要审核项目，如果大于10个就通知管理员审核 */
  @Scheduled(cron = "0 0 0 9-17 * * ")
  public void pendingReview() {
    try {
      Integer number = (Integer) sysProjectService.getPendingReview().getData();
      if (number > 0) {
        ReceiveEmailBo receiveEmailBo = ReceiveEmailBo.builder().build();
        receiveEmailBo.setTitle("项目待审核");
        receiveEmailBo.setReceiverEmail("zhr_java@163.com");
        receiveEmailBo.setContent("spaceObj提醒您，有" + number + "个项目待审核！");
        kafkaSender.send(receiveEmailBo, KafKaTopics.PENDING_REVIEW_PROJECT);
      }
    } catch (Exception e) {
      log.error("get pending review failed !", e.getMessage());
    }
  }
}
