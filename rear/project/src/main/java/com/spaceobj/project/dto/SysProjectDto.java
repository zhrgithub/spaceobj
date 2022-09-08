package com.spaceobj.project.dto;

import com.spaceobj.project.group.AddPageViewsGroup;
import com.spaceobj.project.group.AuditProjectGroup;
import com.spaceobj.project.group.InsertProjectGroup;
import com.spaceobj.project.group.UpdateProjectGroup;
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

  @NotNull(
      message = "项目id是必填项",
      groups = {UpdateProjectGroup.class, AuditProjectGroup.class, AddPageViewsGroup.class})
  private long pId;

  @NotBlank(
      message = "UUID是必填项",
      groups = {UpdateProjectGroup.class})
  private String uuid;

  @NotBlank(
      message = "内容是必填项",
      groups = {InsertProjectGroup.class, UpdateProjectGroup.class})
  private String content;

  @Digits(integer = 9, fraction = 2, message = "预算格式不正确")
  @DecimalMin(value = "0.00", message = "预算格式不正确")
  @NotNull(
      message = "预算是必填项",
      groups = {InsertProjectGroup.class, UpdateProjectGroup.class})
  private BigDecimal price;

  @NotBlank(
      message = "用户ID是必填项",
      groups = {InsertProjectGroup.class, UpdateProjectGroup.class})
  private String releaseUserId;

  private Long pageViews;

  @NotNull(
      message = "审核状态是必选项",
      groups = {AuditProjectGroup.class})
  private Long status;

  @NotBlank(
      message = "IP属地是必填项",
      groups = {InsertProjectGroup.class, UpdateProjectGroup.class})
  private String ipAddress;

  @NotBlank(
      message = "用户昵称是必填项",
      groups = {InsertProjectGroup.class, UpdateProjectGroup.class})
  private String nickname;

  /** 审核内容 */
  @NotBlank(
      message = "审核内容是必填项",
      groups = {AuditProjectGroup.class})
  private String message;

  private Date createTime;
}
