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
  private String content;

  private Integer projectType;

  private String userId;
}
