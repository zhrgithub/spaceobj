package com.spaceobj.user.controller;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 18:32
 */
@Data
public class Demo implements Serializable {


  @TableId(value = "ID",type = IdType.AUTO)
  private String name;

  @TableField(value = "like")
  private String like;
}
