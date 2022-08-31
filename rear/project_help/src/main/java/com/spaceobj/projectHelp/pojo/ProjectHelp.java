package com.spaceobj.projectHelp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

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
  @NotBlank(message = "项目ID是必填项")
  private long pId;

  @TableField(value = "create_user_id")
  @NotBlank(message = "创建者ID是必填项")
  private String createUserId;

  @TableField(value = "hp_number")
  private long hpNumber;

  @TableField(value = "p_content")
  @NotBlank(message = "内容是必填项")
  private String pContent;

  @TableField(value = "p_price")
  @NotBlank(message = "预算是必填项")
  private double pPrice;

  @TableField(value = "p_release_user_id")
  @NotBlank(message = "项目发布人ID是必填项")
  private String pReleaseUserId;

  @TableField(value = "hp_status")
  private Integer hpStatus;
}
