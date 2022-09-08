package com.spaceobj.projectHelp.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.projectHelp.bo.ProjectHelpBo;
import com.spaceobj.domain.ProjectHelp;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
public interface ProjectHelpService extends IService<ProjectHelp> {
  /**
   * 判断创建项目的次数是否大于0
   * 判断其本人是否已经创建过，如果已经创建过，直接返回之前创建的,
   * 否则根据项目id查询项目库（p_Id、create_user_id、p_content、p_price、p_release_user_id）
   * 校验当前id是否在项目库中存在，如果存在则创建，不存在就不要创建，返回请求参数错误
   *
   * @param projectHelpBo
   * @return
   */
  SaResult createProjectHelpLink(ProjectHelpBo projectHelpBo);


  /**
   * 判断是否是其本人创建的如果是他本人创建的，那么本次助力失败，判断用户当天助力次数是否小于等于0，超过返回今天助力次数已经用完，
   * 如果还有助力次数，根据项目ID，助力人的ID，然后返回助力成功，之后消息队列通知助力列表中的项目助力值+1，助力用户的助力值减一，
   * 然后刷新缓存，如果助力满足10个人，邮件通知项目助力成功，那么消息队列先持久化助力表，然后再刷新缓存
   *
   * @param projectHelpBo
   * @return
   */
  SaResult updateProjectHelpNumber(ProjectHelpBo projectHelpBo);

  /**
   * 查询发布的项目助力列表；
   * 设置缓存分页查询；
   *
   * @param projectHelpBo
   * @return
   */
  SaResult projectHelpList(ProjectHelpBo projectHelpBo);
}
