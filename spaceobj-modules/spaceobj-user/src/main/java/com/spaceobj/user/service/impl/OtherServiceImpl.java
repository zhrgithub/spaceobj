package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.nacos.common.utils.StringUtils;
import com.spaceobj.common.core.utils.ExceptionUtil;
import com.spaceobj.common.redis.service.RedisService;
import com.spaceobj.user.constant.RestData;
import com.spaceobj.user.pojo.Other;
import com.spaceobj.user.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:11
 */
@Service
public class OtherServiceImpl implements OtherService {

    private static final String OTHER_INFO = "other_info";

    @Autowired
    private RedisService redisService;

    @Override
    public SaResult updateOther(Other other) {

        try {
            redisService.setCacheObject(OTHER_INFO, other);
        } catch (Exception e) {
            ExceptionUtil.exceptionToString(e);
            e.printStackTrace();
            return SaResult.error("修改失败");
        }

        return SaResult.ok("修改成功").setData(other);
    }

    @Override
    public SaResult getOther() {

        Other other = null;
        //如果缓存中存在key，返回数据
        if (redisService.hasKey(OTHER_INFO)) {
            other = (Other) redisService.getCacheObject(OTHER_INFO, Other.class);

            if (StringUtils.isBlank(other.getWechat())) {
                other.setWechat(RestData.WECHAT);
            }
            if (StringUtils.isBlank(other.getDownloadUrl())) {
                other.setDownloadUrl(RestData.DOWNLOAD_URL);
            }
            if (ObjectUtils.isEmpty(other.getOnline())) {
                other.setOnline(RestData.ONLINE);
            }

            if (ObjectUtils.isEmpty(other.getVersion())) {
                other.setVersion(RestData.version);
            }
            return SaResult.ok().setData(other);
        }
        //缓存中不存在数据，设置成静态资源数据
        other = Other.builder().downloadUrl(RestData.DOWNLOAD_URL).wechat(RestData.WECHAT).online(RestData.ONLINE).version(RestData.version).build();
        redisService.setCacheObject(OTHER_INFO, other);
        return SaResult.ok().setData(other);
    }

}
