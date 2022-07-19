package com.spaceobj.advertise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.advertise.mapper.JdAdvertisMapper;
import com.spaceobj.advertise.pojo.JdAdvertis;
import com.spaceobj.advertise.service.JdAdvertiseService;
import com.spaceobj.advertise.utils.ResultData;
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
    private static final String JDAdvertiseList = "jd_advertise_list";

    private static final Logger LOG = LoggerFactory.getLogger(JdAdvertiseServiceImpl.class);

    @Override
    public ResultData findList() {

        List<JdAdvertis> list = null;
        try {
            Long size = redisTemplate.opsForList().size(JDAdvertiseList);
            if (size == 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                list = jdAdvertisMapper.selectList(queryWrapper);
                redisTemplate.opsForList().rightPushAll("jd_advertise_list", list.toArray());
            } else {
                //可以使用pipeLine来提升性能
                list = redisTemplate.opsForList().range(JDAdvertiseList, 0, -1);
            }
        } catch (Exception e) {
            LOG.error("查询广告数据异常");
            e.printStackTrace();
            return ResultData.error("服务器异常");
        }
        return ResultData.success().setData("jd_advertise_list", list);
    }

    @Override
    public ResultData saveAdvertis(JdAdvertis jdAdvertis) {

        int result = jdAdvertisMapper.insert(jdAdvertis);
        return null;
    }

    @Override
    public ResultData deleteAdvertis(String id) {

        return null;
    }

    @Override
    public ResultData updateAdvertis(JdAdvertis jdAdvertis) {

        return null;
    }

}
