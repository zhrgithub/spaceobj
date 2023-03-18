package com.spaceobj.user.dto;

import com.spaceobj.user.group.sysUser.FindListSysUserGroup;
import com.spaceobj.user.group.sysUser.UpdateSysUserGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserDto implements Serializable {

    @NotBlank(message = "用户id不为空",
            groups = {UpdateSysUserGroup.class})
    private String userId;

    private String inviteUserId;

    @NotBlank(message = "账号不为空",
            groups = {UpdateSysUserGroup.class})
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

    /** 登录id */
    private String loginId;

    /** 用户封禁状态 */
    @NotNull(message = "用户封禁状态不为空",
            groups = {UpdateSysUserGroup.class})
    private Integer disableStatus;

    /** 创建项目的剩余次数 */
    private Integer createProjectHelpTimes;

    /** 当前页 */
    @NotNull(message = "当前页不为空",
            groups = {FindListSysUserGroup.class})
    private Integer currentPage;

    /** 每页大小 */
    @NotNull(message = "每页大小不为空",
            groups = {FindListSysUserGroup.class})
    private Integer pageSize;

    /** 搜索内容 */
    private String content;

    private String email;

    /** 审核内容 */
    private String auditMsg;

    private Integer viewVideoAdvertiseTimes;

}
