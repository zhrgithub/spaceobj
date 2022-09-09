package com.spaceobj.projectHelp.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 12:46
 */
public class RedisKey {
  /** 项目助力列表 */
  public static final String PROJECT_HELP_LIST = "project_help_list";

  /** 系统用户列表 */
  public static final String SYS_USER_LIST = "sys_user_list";

  /** 项目列表 */
  public static final String PROJECT_LIST = "project_list";

  /**
   * 判断MySQL项目助力列表是否是空,默认是非空
   */
  public static Boolean PROJECT_HELP_LIST_OF_MYSQL_IS_EMPTY = false;
}
