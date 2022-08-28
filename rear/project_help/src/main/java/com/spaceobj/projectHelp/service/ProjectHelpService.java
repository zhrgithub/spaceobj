package com.spaceobj.projectHelp.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.projectHelp.pojo.ProjectHelp;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
public interface ProjectHelpService extends IService<ProjectHelp> {
  /**
   * 创建项目助力链接：根据项目id，还需要校验创建者id是否和当前登录用户id一样，
   * 判断其本人是否已经创建过，如果已经创建过，直接返回之前创建的,
   * 否则根据项目id查询项目库（p_Id、create_user_id、p_content、p_price、p_release_user_id）
   * 校验当前id是否在项目库中存在，如果存在则创建，不存在就不要创建，返回请求参数错误
   *
   * @param projectHelp
   * @return
   */
  SaResult createProjectHelpLink(ProjectHelp projectHelp);


  /**
   * 点击项目助力链接：判断用户是否登录，判断是否是其本人创建的如果是他本人创建的助力失败，判断用户当天助力次数是否超过3，超过返回今天助力次数已经用完，
   * 如果还有助力次数，根据项目ID，助力人的ID，然后返回助力成功，之后消息队列通知助力列表中的项目助力值+1，助力用户的助力值减一，
   * 然后刷新缓存，如果助力满足10个人，邮件通知项目助力成功，那么消息队列先持久化助力表，然后再刷新缓存
   *
   * @param projectId
   * @return
   */
  SaResult updateProjectHelpNumber(String projectId);

  /**
   * 查询发布的项目助力列表；
   * 设置分页：分页查询；
   *
   * @return
   */
  SaResult projectHelpList();
}
