package com.spaceobj.user.vo;

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
public class LoginOrRegisterVo {


    /** 操作类型 */
    private Integer operateType;

    /** 账户 */
    private String email;

    /** 密码 */
    private String password;

    /** 电话 */
    private String phoneNumber;

    /** ip */
    private String ip;

    /** ip属地 */
    private String ipTerritory;

    /** 设备类型 */
    private String deviceType;

    /**
     * 邀请人账号id
     */
    private String inviteUserId;

}
