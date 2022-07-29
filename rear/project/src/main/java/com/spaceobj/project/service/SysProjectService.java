package com.spaceobj.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.project.pojo.SysProject;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
public interface SysProjectService extends IService<SysProject> {

  /**
   * 创建项目：生成唯一的UUID（这里的UUID要和项目ID区分），变成审核状态，然后存储到Redis缓存中，消息队列通知MySQL， 然后返回给用户数据，持久化完毕，根据UUID刷新项目ID
   *
   * 校验请求用户的id是否和当前登录用户一样，校验ip属地是否和用户请求的ip属地一样
   *
   * @param sysProject
   * @return
   */
  SaResult addProject(SysProject sysProject);

  /**
   * 修改项目，根据UUID先修改缓存（可以设计多线程根据项目的不同状态进行修改），进入审核中，不可以再次修改，消息队列通知MySQL，
   * 给用户返回Redis中的数据，持久化完毕，待审核项目大于10条邮件通知管理员审核，如果是删除，根据项目UUID修改项目为删除状态，
   * 然后再通过消息队列通知持久化，给用户返回新的列表数据，不需要管理员审核，项目删除或者已成交要通知助力表隐藏
   *
   * 校验项目发起人id是否和当前登录用户的id一样，ip属地是否一样
   *
   * @param sysProject
   * @return
   */
  SaResult updateProject(SysProject sysProject);

  /**
   * 普通用户在首页查询的项目是审核通过的，不包括用户联系方式的
   *
   * @param content
   * @return
   */
  SaResult findList(String content);

  /**
   * 根据发布者id查询项目: 我的发布：根据用户账户查看自己发布的项目,必须经过校验是否为当前登录用户
   *
   * @param releaseUserId
   * @return
   */
  SaResult findListByReleaseUserId(String releaseUserId);

  /**
   * 项目浏览量：根据项目的ID通过消息队列对持久层的浏览量数据修改，然后存储到缓存
   *
   * 校验项目中是否存在这个id
   *
   * @param projectId
   * @return
   */
  SaResult addPageViews(String projectId);


  /**
   *
   * 判断项目状态是否为审核通过，判断是否为其本人发布的，获取项目联系方式:先判断用户是否实名，判断用户助力表中是否已经获取到，
   * 如果没有获取到，那么判断用户的邀请值是否大于0，大于0可以直接获取，消息队列通知MySQL的也要减1，然后缓存减1，助力表添加数据，
   * 否则返回助力链接
   *
   * 校验项目id是否真实存在
   *
   * @param projectId
   * @return
   */
  SaResult getPhoneNumberByProjectId(String projectId);

  /**
   * 管理员审核项目：消息队列先持久化，然后再刷新缓存，然后邮件通知用户审核通过
   *
   * @param sysProject
   * @return
   */
  SaResult auditProject(SysProject sysProject);

  /**
   * 管理员查询项目：管理员可以根据用户提供的项目ID从Redis缓存中查询指定的项目，根据项目状态查询Redis中待审核的项目， 也可以查询Redis全部的项目
   *
   * @param sysProject
   * @return
   */
  SaResult findListForAdmin(SysProject sysProject);
}
