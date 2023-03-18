package com.spaceobj.user.component;

import com.spaceobj.common.core.utils.ExceptionUtil;
import com.spaceobj.user.bo.SysUserBo;
import com.spaceobj.user.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 22:17
 */
@Component("taskJob")
public class TaskJob {

    public static final int RELEASE_PROJECT_TIMES = 10;

    public static final int PROJECT_HELP_TIMES = 3;

    public static final int SEND_CODE_TIMES = 3;

    public static final int CREATE_PROJECT_HELP_TIMES = 20;

    public static final int EDIT_INFO_TIMES = 3;

    public static final int VIEW_VIDEO_ADVERTISE_TIMES = 3;

    Logger LOG = LoggerFactory.getLogger(TaskJob.class);

    @Autowired
    private SysUserService sysUserService;

    /** 每天凌晨触发，每日凌晨将所有用户的发布次数初始化成10，助力次数初始化成10，验证码发送次数3,创建项目助力链接的限度设置成10 */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateUserInfoEveryDay() {

        try {
            SysUserBo sysUserBo = SysUserBo.builder().releaseProjectTimes(RELEASE_PROJECT_TIMES).projectHelpTimes(PROJECT_HELP_TIMES).sendCodeTimes(SEND_CODE_TIMES).createProjectHelpTimes(CREATE_PROJECT_HELP_TIMES).viewVideoAdvertiseTimes(VIEW_VIDEO_ADVERTISE_TIMES).build();
            sysUserService.updateAll(sysUserBo);
        } catch (Exception e) {
            ExceptionUtil.exceptionToString(e);
        }
    }

    /** 每月一号触发，每月一号将用户信息的修改次数初始化成3 */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateUserInfoEveryMonth() {

        try {
            SysUserBo sysUserBo = SysUserBo.builder().editInfoTimes(EDIT_INFO_TIMES).build();
            sysUserService.updateAll(sysUserBo);
        } catch (Exception e) {
            ExceptionUtil.exceptionToString(e);
        }
    }

    /** 朝九晚五，每一个小时判断一下是否需要审核实名认证的用户，如果大于10个就通知管理员审核 */
    @Scheduled(cron = "0 0 0 9-17 * * ")
    public void noticeAuditUserInfo() {

        try {
            sysUserService.noticeAuditUserRealNameInfo();
        } catch (Exception e) {
            ExceptionUtil.exceptionToString(e);
        }
    }

}
