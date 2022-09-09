package com.spaceobj.user.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 12:05
 */
public class KafKaTopics {

  /** 邮箱验证码 */
  public static final String EMAIL_VERIFICATION_CODE = "email_verification_code";

  /** 待审核通知 */
  public static final String AUDIT_NOTICE = "audit_notice";

  /** 用户注册 */
  public static final String ADD_USER = "add_user";

  /** 修改用户信息 */
  public static final String UPDATE_USER = "update_user";

  /** 刷新系统用户缓存信息 */
  public static final String UPDATE_USER_LIST = "update_user_list";
}
