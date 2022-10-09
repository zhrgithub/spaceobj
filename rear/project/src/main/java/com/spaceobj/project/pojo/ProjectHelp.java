package com.spaceobj.project.pojo;

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
public class ProjectHelp {

  private String hpId;

  private String pUUID;

  private String createUserId;

  private Integer hpNumber;

  private String pContent;

  private BigDecimal pPrice;

  private String pReleaseUserId;

  private Integer hpStatus;
}
