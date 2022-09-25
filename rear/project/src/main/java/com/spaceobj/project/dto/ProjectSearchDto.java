package com.spaceobj.project.dto;

import com.spaceobj.project.group.ProjectSearchAdmin;
import com.spaceobj.project.group.ProjectSearchCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author zhr_java@163.com
 * @date 2022/9/5 22:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSearchDto {
  private String content;

  @NotNull(
      message = "查询类型是必填项",
      groups = {ProjectSearchCustomer.class})
  private Integer projectType;

  /** 当前页 */
  @NotNull(
      message = "当前页是必填项",
      groups = {ProjectSearchCustomer.class, ProjectSearchAdmin.class})
  private Integer pageNumber;

  /** 每页条数 */
  @NotNull(
      message = "每页条数是必填项",
      groups = {ProjectSearchCustomer.class, ProjectSearchAdmin.class})
  private Integer pageSize;

  /** 项目id */
  private Long pId;
}
