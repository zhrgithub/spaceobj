package com.redis.common.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 12:47
 */
public class RedisKey {
  /** 系统用户列表 */
  public static final String SYS_USER_LIST = "sys_user_list";

  /** 系统图片列表 */
  public static final String SYS_PHOTO_LIST = "sys_photo_list";

  /** 头像列表同步状态 */
  public static final String PHOTO_LIST_SYNC_STATUS = "photo_list_sync_status";

  /** 京东广告列表 */
  public static final String JD_ADVERTISE_LIST = "jd_advertise_list";

  /** 京东广告同步状态 */
  public static final String JD_ADVERTISE_LIST_SYNC_STATUS = "jd_advertise_list_sync_status";


  /** 项目列表 */
  public static final String PROJECT_LIST = "project_list";

  /** 设置项目列表同步状态的全局key,判断当前缓存是否在同步中,如果在同步中，等待50毫秒，递归一次，否则返回缓存中的信息,默认是没有在同步 */
  public static final String REDIS_PROJECT_SYNC_STATUS = "redis_project_sync_status";

  /** 项目助力列表 */
  public static final String PROJECT_HELP_LIST = "project_help_list";

  /** 项目助力列表同步状态 */
  public static final String PROJECT_HELP_LIST_SYNC_STATUS = "project_help_list_sync_status";

}
