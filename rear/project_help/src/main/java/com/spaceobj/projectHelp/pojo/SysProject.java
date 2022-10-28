package com.spaceobj.projectHelp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysProject implements Serializable {

  /**
   * 项目id
   */
  private long pId;

  /**
   * 项目新增时候的UUID
   */
  private String uuid;

  private String content;

  private BigDecimal price;

  private String releaseUserId;

  private long pageViews;

  private long status;

  private String ipAddress;

  private String nickname;

  /** 审核内容 */
  private String message;

  private LocalDateTime createTime;

  private long version;
}
