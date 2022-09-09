package com.spaceobj.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * @author zhr
 */
@Data
@Builder
@TableName(value = "project_help")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHelp {

  @TableId(value = "hp_id")
  private String hpId;

  @TableField(value = "p_Id")
  private long pId;

  @TableField(value = "create_user_id")
  private String createUserId;

  @TableField(value = "hp_number")
  private long hpNumber;

  @TableField(value = "p_content")
  private String pContent;

  @TableField(value = "p_price")
  private BigDecimal pPrice;

  @TableField(value = "p_release_user_id")
  private String pReleaseUserId;

  @TableField(value = "hp_status")
  private Integer hpStatus;
}
