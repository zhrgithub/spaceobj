package com.spaceobj.other.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.nacos.common.utils.StringUtils;
import com.spaceobj.other.constant.RestData;
import com.spaceobj.domain.Other;
import com.spaceobj.other.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:11
 */
@Service
public class OtherServiceImpl implements OtherService {

    private final static String OTHER_INFO = "other_info";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public SaResult updateOther(Other other) {

        try {
            redisTemplate.boundValueOps(OTHER_INFO).set(other);
        } catch (Exception e) {
            e.printStackTrace();
            return SaResult.error("修改失败");
        }

        return SaResult.ok();
    }

    @Override
    public SaResult getOther() {

        Other other = null;

        if (redisTemplate.hasKey(OTHER_INFO)) {
            other = (Other) redisTemplate.boundValueOps(OTHER_INFO).get();

            if (StringUtils.isBlank(other.getWechat())) {
                other.setWechat(RestData.WECHAT);
            }
            if (StringUtils.isBlank(other.getDownloadUrl())) {
                other.setDownloadUrl(RestData.DOWNLOAD_URL);
            }
            return SaResult.ok().setData(other);
        }
        other = Other.builder().downloadUrl(RestData.DOWNLOAD_URL).wechat(RestData.WECHAT).build();

        return SaResult.ok().setData(other);
    }

}
