package com.spaceobj.project.bo;

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

  /** requestIP */
  private String requestIp;

  /** 新密码 */
  private String newPassword;

  /** 当前登录id */
  private String loginId;

  /** 用户封禁状态 */
  private Integer disableStatus;

  /** 创建项目的剩余次数 */
  private Integer createProjectHelpTimes;

  /** 当前页 */
  private Integer currentPage;

  /** 每页大小 */
  private Integer pageSize;
}
