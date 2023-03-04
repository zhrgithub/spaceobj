package com.spaceobj.user.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhr_java@163.com
 * @date 2022/11/8 19:33
 */
@Data
@Builder
public class LoginByEmailBo {

    /** 邮箱 */
    private String email;

    /** 邮箱验证码 */
    private String emailCode;

    /** ip属地 */
    private String ipTerritory;

    /** 设备类型 */
    private String deviceType;

    /** 邀请人账号id */
    private String inviteUserId;

    /** 昵称 */
    private String nickName;

}
