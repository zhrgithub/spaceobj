package com.spaceobj.advertise.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.advertise.bo.JdAdvertisBo;
import com.spaceobj.advertise.constant.RedisKey;
import com.spaceobj.advertise.mapper.JdAdvertisMapper;
import com.spaceobj.domain.JdAdvertis;
import com.spaceobj.advertise.service.JdAdvertiseService;
import com.spaceobj.advertise.utils.BeanConvertToTargetUtils;
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

    private static final Logger LOG = LoggerFactory.getLogger(JdAdvertiseServiceImpl.class);

    public boolean getJdAdvertiseListSyncStatus() {

        boolean hasKey = redisTemplate.hasKey(RedisKey.JD_ADVERTISE_LIST_SYNC_STATUS);
        if (!hasKey) {
            redisTemplate.opsForValue().set(RedisKey.JD_ADVERTISE_LIST_SYNC_STATUS, false);
            return false;
        } else {
            return (boolean) redisTemplate.opsForValue().get(RedisKey.JD_ADVERTISE_LIST_SYNC_STATUS);
        }
    }

    /**
     * 同步数据到Redis缓存，并返回查询到的数据
     *
     * @return
     */
    private List<JdAdvertis> syncAdvertiseList() {

        List<JdAdvertis> list = null;
        redisTemplate.delete(RedisKey.JD_ADVERTISE_LIST);
        QueryWrapper<JdAdvertis> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        list = jdAdvertisMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(RedisKey.JD_ADVERTISE_LIST, list.toArray());
        return list;
    }

    @Override
    public SaResult findList() {

        List<JdAdvertis> list = null;
        try {

            Long size = redisTemplate.opsForList().size(RedisKey.JD_ADVERTISE_LIST);
            if (size == 0) {
                if (this.getJdAdvertiseListSyncStatus()) {
                    Thread.sleep(50);
                    this.findList();
                } else {
                    redisTemplate.opsForValue().set(RedisKey.JD_ADVERTISE_LIST_SYNC_STATUS, true);
                    list = this.syncAdvertiseList();
                    redisTemplate.opsForValue().set(RedisKey.JD_ADVERTISE_LIST_SYNC_STATUS, false);

                }
            } else {
                list = redisTemplate.opsForList().range(RedisKey.JD_ADVERTISE_LIST, 0, -1);
            }
            return SaResult.ok().setData(list);
        } catch (Exception e) {
            LOG.error("search advertise list error", e.getMessage());
            return SaResult.error("广告列表查询异常");
        }
    }

    @Override
    public SaResult saveAdvertise(JdAdvertisBo jdAdvertisBo) {

        JdAdvertis jdAdvertis = JdAdvertis.builder().build();
        BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertisBo, jdAdvertis);

        try {
            int result = jdAdvertisMapper.insert(jdAdvertis);
            if (result == 1) {
                //刷新缓存
                this.updateRedis();
            } else {

                LOG.error("Logic add advertise error");
                return SaResult.error("新增失败");
            }
            return SaResult.ok("新增成功");
        } catch (Exception e) {

            LOG.error("add advertise error", e.getMessage());
            return SaResult.error("新增广告失败,服务器异常！");
        }
    }

    @Override
    public SaResult deleteAdvertise(long id) {

        try {
            System.out.println(id);
            int result = jdAdvertisMapper.deleteById(id);
            if (result == 1) {
                this.updateRedis();
            } else {
                LOG.error("Logic delete advertise error !");
                return SaResult.error("删除失败");
            }
            return SaResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("delete advertise error !", e.getMessage());
            return SaResult.error("删除广告失败，服务器异常！");
        }
    }

    @Override
    public SaResult updateAdvertise(JdAdvertisBo jdAdvertisBo) {

        JdAdvertis jdAdvertis = JdAdvertis.builder().build();
        BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertisBo, jdAdvertis);

        try {
            int result = jdAdvertisMapper.updateById(jdAdvertis);
            if (result == 1) {
                this.updateRedis();
            } else {
                LOG.error("Logic update advertise error !");
                return SaResult.error("更新失败");
            }
            return SaResult.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Logic update advertise error !", e.getMessage());
            return SaResult.error("广告更新失败！服务器异常！");
        }
    }

    /** 刷新Redis缓存 */
    private void updateRedis() {

        redisTemplate.delete(RedisKey.JD_ADVERTISE_LIST);
        List<JdAdvertis> jdAdvertiseList;
        QueryWrapper<JdAdvertis> queryWrapper = new QueryWrapper();
        jdAdvertiseList = jdAdvertisMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(RedisKey.JD_ADVERTISE_LIST, jdAdvertiseList.toArray());
    }

}
