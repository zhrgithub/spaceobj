package com.spaceobj.projectHelp.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 12:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectHelpBo {

  /** 用户id */
  private String userId;

  /** 项目id */
  private long pId;

  /** 项目内容 */
  private String pContent;

  /** 项目预算 */
  private double pPrice;

  /** 项目发起人 */
  private String pReleaseUserId;
}
