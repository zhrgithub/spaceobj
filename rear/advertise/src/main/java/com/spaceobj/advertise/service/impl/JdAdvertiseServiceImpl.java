package com.spaceobj.advertise.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.advertise.mapper.JdAdvertisMapper;
import com.spaceobj.advertise.pojo.JdAdvertis;
import com.spaceobj.advertise.service.JdAdvertiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/7/18 14:53
 */
@Service
public class JdAdvertiseServiceImpl extends ServiceImpl<JdAdvertisMapper, JdAdvertis> implements JdAdvertiseService {

    @Autowired
    private JdAdvertisMapper jdAdvertisMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 京东广告列表
     */
    private static final String JD_ADVERTISE_LIST = "jd_advertise_list";

    private static final Logger LOG = LoggerFactory.getLogger(JdAdvertiseServiceImpl.class);

    @Override
    public SaResult findList() {

        List<JdAdvertis> list = null;
        try {
            Long size = redisTemplate.opsForList().size(JD_ADVERTISE_LIST);
            if (size == 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                list = jdAdvertisMapper.selectList(queryWrapper);
                redisTemplate.opsForList().rightPushAll(JD_ADVERTISE_LIST, list.toArray());
            } else {
                //可以使用pipeLine来提升性能
                list = redisTemplate.opsForList().range(JD_ADVERTISE_LIST, 0, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SaResult.error("广告列表查询异常");
        }
        return SaResult.ok().setData(list);
    }

    @Override
    public SaResult saveAdvertis(JdAdvertis jdAdvertis) {

        int result = jdAdvertisMapper.insert(jdAdvertis);
        return null;
    }

    @Override
    public SaResult deleteAdvertis(String id) {

        return null;
    }

    @Override
    public SaResult updateAdvertis(JdAdvertis jdAdvertis) {

        return null;
    }

}
