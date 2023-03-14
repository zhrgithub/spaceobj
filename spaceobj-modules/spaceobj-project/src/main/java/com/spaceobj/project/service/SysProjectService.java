package com.spaceobj.project.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.project.bo.GetPhoneNumberBo;
import com.spaceobj.project.bo.ProjectSearchBo;
import com.spaceobj.project.pojo.SysProject;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
public interface SysProjectService extends IService<SysProject> {

    /**
     * 创建项目：生成唯一的UUID（这里的UUID要和项目ID区分），变成审核状态，然后存储到Redis缓存中，消息队列通知MySQL， 然后返回给用户数据，持久化完毕，根据UUID刷新项目ID
     *
     * @param sysProject
     *
     * @return
     */
    SaResult addProject(SysProject sysProject);

    /**
     * 修改项目，根据UUID先修改缓存（可以设计多线程根据项目的不同状态进行修改），进入审核中，不可以再次修改，消息队列通知MySQL，
     * 给用户返回Redis中的数据，持久化完毕，待审核项目大于10条邮件通知管理员审核，如果是删除，根据项目UUID修改项目为删除状态，
     * 然后再通过消息队列通知持久化，给用户返回新的列表数据，不需要管理员审核，项目删除或者已成交要通知助力表隐藏
     *
     * <p>通过定时任务提醒管理员审核项目：消息队列先持久化，然后再刷新缓存，然后邮件通知用户审核通过
     *
     * @param sysProject
     *
     * @return
     */
    SaResult updateProject(SysProject sysProject);

    /**
     * 管理员审核项目
     *
     * @param project
     *
     * @return
     */
    SaResult auditProject(SysProject project);

    /**
     * 普通用户在首页查询的项目是审核通过的，不包括用户联系方式的； 设置分页查询
     * 项目类型：0表示首页所有已经审核通过的信息，1表示查看自己发布的项目信息（网关要校验是否为当前登录用户），2表示管理员查询的项目信息 设置查询条件：项目id，预算，项目内容
     *
     * @param projectSearchBo 查询条件实体类
     *
     * @return
     */
    SaResult findList(ProjectSearchBo projectSearchBo);

    /**
     * 管理员查询所有项目列表
     *
     * @param projectSearchBo
     *
     * @return
     */
    SaResult queryListAdmin(ProjectSearchBo projectSearchBo);

    /**
     * 项目浏览量：根据项目的ID通过消息队列对持久层的浏览量数据修改，然后存储到缓存
     *
     * <p>校验项目中是否存在这个uuid
     *
     * @param uuid
     *
     * @return
     */
    void addPageViews(String uuid);

    /**
     * 判断是否为其本人发布的, 判断项目状态是否为审核通过，判断用户是否实名，判断助力表是否有该数据，判断用户助力表中是否已经获取到，
     * 判断用户的邀请值是否大于0，大于0可以直接获取，消息队列通知MySQL的也要减1，然后缓存减1，判断助力表中是否有该数据，有则更新，没有就在助力表添加该用户已获取数据， 否则返回助力链接
     *
     * <p>校验项目id是否真实存在
     *
     * @param getPhoneNumberBo
     *
     * @return
     */
    SaResult getPhoneNumberByProjectId(GetPhoneNumberBo getPhoneNumberBo);

    /**
     * 获取当前待审核项目的数量
     *
     * @return
     */
    SaResult getPendingReview();

    /**
     * 根据项目的UUID获取项目信息
     *
     * @param uuid
     *
     * @return
     */
    SysProject getProjectByUUID(String uuid);



    String getPhoneNumberByProjectUUID(String uuid);

}
