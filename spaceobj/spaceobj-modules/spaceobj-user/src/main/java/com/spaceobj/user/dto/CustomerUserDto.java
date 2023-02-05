package com.spaceobj.user.dto;

import com.spaceobj.user.group.customer.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author zhr_java@163.com
 * @date 2022/8/30 13:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserDto {

  /** 用户登录凭证。开发者需要在开发者服务器后台，使用 code 换取 openid 和 session_key 等信息 */
  @NotBlank(
      message = "code不为空",
      groups = {LoginByWechat.class, BindWechat.class, LoginByQQ.class})
  private String code;

  /** 操作类型 */
  @NotNull(
      message = "操作类型不为空",
      groups = {LoginOrRegisterGroup.class})
  private Integer operateType;

  /** 账户 */
  @NotBlank(
      message = "登录账户不为空",
      groups = {
        LoginOrRegisterGroup.class,
        LoginOutGroup.class,
        GetUserInfoGroup.class,
        SendMailGroup.class,
        RealNameGroup.class
      })
  private String account;

  /** 密码 */
  @NotBlank(
      message = "密码不为空",
      groups = {LoginOrRegisterGroup.class})
  private String password;

  /** 电话 */
  @NotBlank(
      message = "电话不为空",
      groups = {UpdateUserInfoGroup.class})
  private String phoneNumber;

  /** ip */
  private String ip;

  /** requestIP */
  @NotBlank(
      message = "请求ip不为空",
      groups = {})
  private String requestIp;

  /** ip属地 */
  @NotBlank(
      message = "ip属地不为空",
      groups = {
        LoginOrRegisterGroup.class,
        UpdateUserInfoGroup.class,
        LoginByWechat.class,
        LoginByQQ.class
      })
  private String ipTerritory;

  /** 设备类型 */
  @NotBlank(
      message = "登录设备类型不为空",
      groups = {LoginOrRegisterGroup.class, LoginByWechat.class, LoginByQQ.class})
  private String deviceType;

  /** 邀请人账号id */
  private String inviteUserId;

  @NotBlank(
      message = "用户id不为空",
      groups = {BindWechat.class})
  private String userId;

  @NotNull(
      message = "邮箱验证码不为空",
      groups = {ResetPassWordGroup.class})
  private String emailCode;

  private String token;

  private String openId;

  private Integer assistValue;

  private Integer invitationValue;

  private String userType;

  private String userRights;

  @NotBlank(
      message = "真实姓名不为空",
      groups = {RealNameGroup.class})
  private String username;

  private String nickName;

  private String photoUrl;

  private Integer onlineStatus;

  private Integer userInfoEditStatus;

  @NotBlank(
      message = "身份证号不为空",
      groups = {RealNameGroup.class})
  private String idCardNum;

  @NotBlank(
      message = "请上传身份证照片",
      groups = {RealNameGroup.class})
  private String idCardPic;

  private Integer realNameStatus;

  private Integer editInfoTimes;

  private Integer sendCodeTimes;

  private Integer releaseProjectTimes;

  private Integer projectHelpTimes;

  /** 新密码 */
  @NotBlank(
      message = "新密码不为空",
      groups = {ResetPassWordGroup.class})
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

  @NotBlank(
      message = "邮箱不为空",
      groups = {UpdateUserInfoGroup.class})
  private String email;
}
