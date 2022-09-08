package com.spaceobj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysProject implements Serializable {

  private long pId;

  private String uuid;

  private String content;

  private BigDecimal price;

  private String releaseUserId;

  private long pageViews;

  private long status;

  private String ipAddress;

  private String nickname;

  private String message;

  private Date createTime;
}
