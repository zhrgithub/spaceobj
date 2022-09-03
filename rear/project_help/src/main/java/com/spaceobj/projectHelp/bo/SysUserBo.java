package com.spaceobj.projectHelp.bo;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserBo implements Serializable {

  private String userId;

  private String inviteUserId;

  private String account;

  private String emailCode;

  private String password;

  private String token;

  private String openId;

  private String phoneNumber;

  private Integer assistValue;

  private Integer invitationValue;

  private String userType;

  private String userRights;

  private String username;

  private String nickName;

  private String photoUrl;

  private Integer onlineStatus;

  private Integer userInfoEditStatus;

  private String idCardNum;

  private String idCardPic;

  private Integer realNameStatus;

  private String ip;

  private String ipTerritory;

  private Integer editInfoTimes;

  private Integer sendCodeTimes;

  private Integer releaseProjectTimes;

  private Integer projectHelpTimes;


  private String deviceType;

  private Integer createProjectHelpTimes;
}
