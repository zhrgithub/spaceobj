package com.spaceobj.gateway.config;

import cn.dev33.satoken.id.SaIdUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Id-Token，定时刷新
 *
 * @author zhr_java@163.com
 * @date 2022/9/11 17:46
 */
@Configuration
public class SaIdTokenRefreshTask {

    /** 从 0 分钟开始 每隔 5 分钟执行一次 Id-Token */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshToken() {

        SaIdUtil.refreshToken();
    }

}
