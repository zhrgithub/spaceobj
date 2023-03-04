package com.spaceobj.user.dto;

import com.spaceobj.user.group.customer.LoginByEmailGroup;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zhr_java@163.com
 * @date 2022/11/8 19:33
 */
@Data
@Builder
public class LoginByEmailDto {

    /** 邮箱 */
    @NotBlank(message = "邮箱不为空",
            groups = {LoginByEmailGroup.class})
    private String email;

    /** 邮箱验证码 */
    @NotBlank(message = "邮箱验证码不为空",
            groups = {LoginByEmailGroup.class})
    private String emailCode;

    /** ip属地 */
    private String ipTerritory;

    /** 设备类型 */
    private String deviceType;

    /** 邀请人账号id */
    private String inviteUserId;

    /** 昵称 */
    @NotBlank(message = "邮箱不为空",
            groups = {LoginByEmailGroup.class})
    private String nickName;

}
