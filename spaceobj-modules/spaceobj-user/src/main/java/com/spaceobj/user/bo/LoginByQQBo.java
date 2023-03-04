package com.spaceobj.user.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhr_java@163.com
 * @date 2022/10/10 23:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginByQQBo {

    /** 用户登录凭证。开发者需要在开发者服务器后台，使用 code 换取 openid 和 session_key 等信息 */
    private String code;

    /** ip */
    private String ip;

    /** ip属地 */
    private String ipTerritory;

    /** 设备类型 */
    private String deviceType;

    /** 邀请人账号id */
    private String inviteUserId;

    /** 昵称 */
    private String nickName;

}
