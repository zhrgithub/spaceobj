package com.spaceobj.project.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHelpBo {

  private String hpId;

  private long pId;

  private long createUserId;

  private long hpNumber;

  private String pContent;

  private double pPrice;

  private long pReleaseUserId;

  private long hpStatus;
}
