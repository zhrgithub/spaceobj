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
                list = redisTemplate.opsForList().range(JD_ADVERTISE_LIST, 0, -1);
            }
            return SaResult.ok().setData(list);
        } catch (Exception e) {
            LOG.error("search advertise list error", e.getMessage());
            return SaResult.error("广告列表查询异常");
        }
    }

    @Override
    public SaResult saveAdvertise(JdAdvertis jdAdvertis) {

        try {
            int result = jdAdvertisMapper.insert(jdAdvertis);
            if (result == 1) {
                //刷新缓存
                this.updateRedis();
            } else {
                LOG.error("Logic add advertise error");
            }
        } catch (Exception e) {
            LOG.error("add advertise error", e.getMessage());
            return SaResult.error("新增广告失败,服务器异常！");
        }

        return null;
    }

    @Override
    public SaResult deleteAdvertise(String id) {

        try {
            int result = jdAdvertisMapper.deleteById(id);
            if (result == 1) {
                this.updateRedis();
            } else {
                LOG.error("Logic delete advertise error !");
            }
            return SaResult.ok("删除成功！");
        } catch (Exception e) {
            LOG.error("delete advertise error !", e.getMessage());
            return SaResult.error("删除广告失败，服务器异常！");
        }
    }

    @Override
    public SaResult updateAdvertise(JdAdvertis jdAdvertis) {

        try {
            int result = jdAdvertisMapper.updateById(jdAdvertis);
            if (result == 1) {
                this.updateRedis();
            } else {
                LOG.error("Logic update advertise error !");
            }
            return SaResult.ok("广告更新成功");
        } catch (Exception e) {
            LOG.error("Logic update advertise error !", e.getMessage());
            return SaResult.error("广告更新失败！服务器异常！");
        }
    }

    /** 刷新Redis缓存 */
    private void updateRedis() {

        redisTemplate.delete(JD_ADVERTISE_LIST);
        List<JdAdvertis> jdAdvertiseList;
        QueryWrapper<JdAdvertis> queryWrapper = new QueryWrapper();
        jdAdvertiseList = jdAdvertisMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(JD_ADVERTISE_LIST, jdAdvertiseList.toArray());
    }

}
