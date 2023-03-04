package com.spaceobj.common.kafka.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/9/28 13:17
 */
public class KafKaTopics {

    /** 修改用户信息 */
    public static final String UPDATE_USER = "update_user";

    /** 项目助力列表更新信息 */
    public static final String UPDATE_HELP_PROJECT = "update_help_project";

    /** 项目助力列表新增信息 */
    public static final String ADD_HELP_PROJECT = "add_help_project";

    /** 待审核项目通知 */
    public static final String PENDING_REVIEW_PROJECT = "pending_review_project";

    /** 项目助力成功邮箱通知 */
    public static final String HELP_PROJECT_SUCCESSFUL = "help_project_successful";

    /** 刷新系统用户缓存信息 */
    public static final String UPDATE_USER_LIST = "update_user_list";

    /** 邮箱验证码 */
    public static final String EMAIL_VERIFICATION_CODE = "email_verification_code";

    /** 待审核通知 */
    public static final String AUDIT_NOTICE = "audit_notice";

    /** 邀请人邀请值加一 */
    public static final String INVITER_VALUE_ADD = "inviter_value_add";

    /** 审核结果通知 */
    public static final String AUDIT_RESULT_NOTICE = "audit_result_notice";

}
