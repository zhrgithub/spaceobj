package com.spaceobj.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

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
  @NotBlank(message = "内容是必填项")
  private String content;

  @NotNull(message = "项目类型是必填项")
  private Integer projectType;

  private String userId;
}
