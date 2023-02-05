package com.spaceobj.common.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr_java@163.com
 * @date 2022/10/24 19:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPermission {

  /** 用户角色权限 */
  private String userType;

  /** 用户接口权限 */
  private String userRights;
}
