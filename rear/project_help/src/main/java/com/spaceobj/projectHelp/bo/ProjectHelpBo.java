package com.spaceobj.projectHelp.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 12:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectHelpBo {

  /** 项目助力id */
  private String hpId;

  /** 创建助力链接的用户id,也是当前登录者id */
  private String createUserId;

  /** 项目id */
  private long pId;

  /** 项目内容 */
  private String pContent;

  /** 项目预算 */
  private BigDecimal pPrice;

}
