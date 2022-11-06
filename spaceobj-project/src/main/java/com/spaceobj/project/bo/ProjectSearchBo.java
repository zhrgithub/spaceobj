package com.spaceobj.project.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr_java@163.com
 * @date 2022/9/5 22:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSearchBo {

  /** 搜索内容 */
  private String content;

  /** 查询类型：0表示查询首页，1表示查询自己的 */
  private Integer projectType;

  /** 发布者id */
  private String userId;

  /** 当前页 */
  private Integer currentPage;

  /** 每页条数 */
  private Integer pageSize;

  /** 项目id */
  private Long pId;
}
