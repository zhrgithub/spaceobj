package com.spaceobj.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhr
 */
@Data
@TableName(value = "sys_user")
public class SysUser implements Serializable {

  @TableField(value = "user_id")
  private long userId;

  @TableField(value = "account")
  private String account;

  @TableField(value = "password")
  private String password;

  @TableField(value = "token")
  private String token;

  @TableField(value = "open_id")
  private String openId;

  @TableField(value = "phone_number")
  private String phoneNumber;

  @TableField(value = "invitation_value")
  private long invitationValue;

  @TableField(value = "user_type")
  private String userType;

  @TableField(value = "user_rights")
  private String userRights;

  @TableField(value = "username")
  private String username;

  @TableField(value = "nick_name")
  private String nickName;

  @TableField(value = "photo_url")
  private String photoUrl;

  @TableField(value = "online_status")
  private String onlineStatus;

  @TableField(value = "id_card_num")
  private String idCardNum;

  @TableField(value = "real_name_status")
  private String realNameStatus;

  @TableField(value = "ip")
  private String ip;

  @TableField(value = "ip_territory")
  private String ipTerritory;

  @TableField(value = "device_type")
  private String deviceType;

}
