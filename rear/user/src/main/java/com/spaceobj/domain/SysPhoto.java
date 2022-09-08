package com.spaceobj.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhr
 */
@Data
@TableName(value = "sys_photo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPhoto implements Serializable {

  @TableId(value = "photo_id")
  private Long photoId;

  @TableField(value = "photo_url")
  private String photoUrl;
}
