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
@TableName(value = "sys_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {

  @TableId(value = "user_id")
  private String userId;

  @TableField(value = "invite_user_id")
  private String inviteUserId;

  @TableField(value = "account")
  private String account;

  @TableField(value = "email_code")
  private String emailCode;

  @TableField(value = "password")
  private String password;

  @TableField(value = "token")
  private String token;

  @TableField(value = "open_id")
  private String openId;

  @TableField(value = "phone_number")
  private String phoneNumber;

  /** 助力值 */
  @TableField(value = "assist_value")
  private Integer assistValue;

  @TableField(value = "invitation_value")
  private Integer invitationValue;

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
  private Integer onlineStatus;

  @TableField(value = "user_info_edit_status")
  private Integer userInfoEditStatus;

  @TableField(value = "id_card_num")
  private String idCardNum;

  @TableField(value = "id_card_pic")
  private String idCardPic;

  @TableField(value = "real_name_status")
  private Integer realNameStatus;

  @TableField(value = "ip")
  private String ip;

  @TableField(value = "ip_territory")
  private String ipTerritory;

  @TableField(value = "edit_info_times")
  private Integer editInfoTimes;

  @TableField(value = "send_code_times")
  private Integer sendCodeTimes;

  @TableField(value = "release_project_times")
  private Integer releaseProjectTimes;

  @TableField(value = "project_help_times")
  private Integer projectHelpTimes;

  @TableField(value = "device_type")
  private String deviceType;

  @TableField(value = "create_project_help_times")
  private Integer createProjectHelpTimes;

  @TableField(value = "disable_status")
  private Integer disableStatus;
}
