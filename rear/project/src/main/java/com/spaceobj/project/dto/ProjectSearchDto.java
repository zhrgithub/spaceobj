package com.spaceobj.project.dto;

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

  @NotNull(message = "查询类型是必填项")
  private Integer projectType;
}
