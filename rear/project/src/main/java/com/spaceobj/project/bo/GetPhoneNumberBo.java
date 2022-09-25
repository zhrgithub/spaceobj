package com.spaceobj.project.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr_java@163.com
 * @date 2022/9/9 01:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPhoneNumberBo {

  private long projectId;

  /** 当前登录用户id */
  private String userId;

}
