package com.spaceobj.project.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
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
public class SysProjectDto implements Serializable {

  @TableId(value = "p_id")
  private long pId;

  @TableField(value = "p_uuid")
  private String uuid;

  @TableField(value = "p_content")
  @NotBlank(message = "内容是必填项")
  private String content;

  @TableField(value = "p_price")
  @Digits(integer = 9, fraction=2, message = "amount格式不正确")
  @DecimalMin(value = "0.00", message = "amount格式不正确")
  @NotNull(message = "预算是必填项")
  private BigDecimal price;

  @TableField(value = "p_release_user_id")
  @NotBlank(message = "用户ID是必填项")
  private String releaseUserId;

  @TableField(value = "p_page_views")
  private long pageViews;

  @TableField(value = "p_status")
  private long status;

  @TableField(value = "p_ip_address")
  @NotBlank(message = "IP属地是必填项")
  private String ipAddress;

  @TableField(value = "p_nick_name")
  @NotBlank(message = "用户昵称是必填项")
  private String nickname;

  /** 审核内容 */
  @TableField(value = "p_message")
  private String message;

  @TableField(value = "create_time")
  private Date createTime;
}
