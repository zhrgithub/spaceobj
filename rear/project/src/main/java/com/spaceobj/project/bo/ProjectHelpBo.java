package com.spaceobj.project.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

  private String createUserId;

  private Integer hpNumber;

  private String pContent;

  private BigDecimal pPrice;

  private String pReleaseUserId;

  private Integer hpStatus;
}
