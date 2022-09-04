package com.spaceobj.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr_java@163.com
 * @date 2022/8/30 13:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserDto {

  /** 操作类型 */
  private Integer operateType;

  /** 账户 */
  private String account;

  /** 密码 */
  private String password;

  /** 电话 */
  private String phoneNumber;

  /** ip */
  private String ip;

  /** requestIP */
  private String requestIp;

  /** ip属地 */
  private String ipTerritory;

  /** 设备类型 */
  private String deviceType;

  /** 邀请人账号id */
  private String inviteUserId;

  private String userId;

  private String emailCode;

  private String token;

  private String openId;

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

  private Integer editInfoTimes;

  private Integer sendCodeTimes;

  private Integer releaseProjectTimes;

  private Integer projectHelpTimes;

  /** 新密码 */
  private String newPassword;

  /** 登录id */
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
