package com.spaceobj.project.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

  @TableField(value = "p_uuid")
  private String pUUID;

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

  @TableField(value = "create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @TableField(value = "update_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  @TableField(value = "version")
  private long version;


  @TableField(value = "ip_territory")
  private String ipTerritory;

  @TableField(value = "hp_create_nick_name")
  private String hpCreateNickName;

  @TableField(value = "project_create_nick_name")
  private String projectCreateNickName;


}
