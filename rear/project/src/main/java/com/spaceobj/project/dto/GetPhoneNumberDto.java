package com.spaceobj.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author zhr_java@163.com
 * @date 2022/9/9 01:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPhoneNumberDto {

  @NotNull(message = "项目id不为空")
  private long projectId;

  private String userId;
}
