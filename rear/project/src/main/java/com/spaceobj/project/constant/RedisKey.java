package com.spaceobj.project.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 12:47
 */
public class RedisKey {

  /** 设置项目列表的key过期时间 */
  public static final int PROJECT_LIST_EXPIRE_TIME = 5;

  /** 项目列表 */
  public static final String PROJECT_LIST = "project_list";

  /** 设置项目列表同步状态的全局key,判断当前缓存是否在同步中,如果在同步中，等待50毫秒，递归一次，否则返回缓存中的信息,默认是没有在同步 */
  public static final String REDIS_PROJECT_SYNC_STATUS = "redis_project_sync_status";

  /** 项目助力列表 */
  public static final String PROJECT_HELP_LIST = "project_help_list";
}
