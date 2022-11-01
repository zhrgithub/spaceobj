package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.common.service.RedisService;
import com.redis.common.service.RedissonService;
import com.spaceobj.user.bo.JdAdvertiseBo;
import com.spaceobj.user.constant.RedisKey;
import com.spaceobj.user.mapper.JdAdvertiseMapper;
import com.spaceobj.user.pojo.JdAdvertise;
import com.spaceobj.user.service.JdAdvertiseService;
import com.spaceobj.user.utils.BeanConvertToTargetUtils;
import com.spaceobj.user.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/7/18 14:53
 */
@Service
public class JdAdvertiseServiceImpl extends ServiceImpl<JdAdvertiseMapper, JdAdvertise>
    implements JdAdvertiseService {

  @Autowired private JdAdvertiseMapper jdAdvertiseMapper;

  @Autowired private RedisService redisService;

  @Autowired private RedissonService redissonService;

  private static final Logger LOG = LoggerFactory.getLogger(JdAdvertiseServiceImpl.class);

  /**
   * 同步数据到Redis缓存，并返回查询到的数据
   *
   * @return
   */
  private List<JdAdvertise> getJdAdvertiseList() {

    List<JdAdvertise> list = null;
    try {
      boolean hasKey = redisService.hasKey(RedisKey.JD_ADVERTISE_LIST);
      if (hasKey) {
        return redisService.getCacheList(RedisKey.JD_ADVERTISE_LIST, JdAdvertise.class);
      } else {
        boolean flag = redissonService.tryLock(RedisKey.JD_ADVERTISE_LIST_SYNC_STATUS);
        if (!flag) {
          return null;
        } else {
          hasKey = redisService.hasKey(RedisKey.JD_ADVERTISE_LIST);
          if (hasKey) {
            return redisService.getCacheList(RedisKey.JD_ADVERTISE_LIST, JdAdvertise.class);
          }
          redisService.deleteObject(RedisKey.JD_ADVERTISE_LIST);
          QueryWrapper<JdAdvertise> queryWrapper = new QueryWrapper();
          list = jdAdvertiseMapper.selectList(queryWrapper);
          redisService.setCacheList(RedisKey.JD_ADVERTISE_LIST, list);
          return list;
        }
      }
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public SaResult findList() {

    List<JdAdvertise> list = null;
    try {
      list = getJdAdvertiseList();
      if (list == null) {
        return SaResult.error("访问超时稍后再试");
      }
      return SaResult.ok().setData(list);
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      LOG.error("search advertise list error", e.getMessage());
      return SaResult.error("广告列表查询异常");
    }
  }

  @Override
  public SaResult saveAdvertise(JdAdvertiseBo jdAdvertiseBo) {

    JdAdvertise jdAdvertise = JdAdvertise.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertiseBo, jdAdvertise);

    try {
      int result = jdAdvertiseMapper.insert(jdAdvertise);
      if (result == 1) {
        // 删除缓存
        redisService.deleteObject(RedisKey.JD_ADVERTISE_LIST);
      } else {

        LOG.error("Logic add advertise error");
        return SaResult.error("新增失败");
      }
      return SaResult.ok("新增成功");
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      LOG.error("add advertise error", e.getMessage());
      return SaResult.error("新增广告失败,服务器异常！");
    }
  }

  @Override
  public SaResult deleteAdvertise(long id) {

    try {
      int result = jdAdvertiseMapper.deleteById(id);
      if (result == 1) {
        // 删除缓存
        redisService.deleteObject(RedisKey.JD_ADVERTISE_LIST);
      } else {
        LOG.error("Logic delete advertise error !");
        return SaResult.error("删除失败");
      }
      return SaResult.ok("删除成功！");
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      LOG.error("delete advertise error !", e.getMessage());
      return SaResult.error("删除广告失败，服务器异常！");
    }
  }

  @Override
  public SaResult updateAdvertise(JdAdvertiseBo jdAdvertiseBo) {

    JdAdvertise jdAdvertise = JdAdvertise.builder().build();
    BeanConvertToTargetUtils.copyNotNullProperties(jdAdvertiseBo, jdAdvertise);

    try {
      int result = jdAdvertiseMapper.updateById(jdAdvertise);
      if (result == 1) {
        // 删除缓存
        redisService.deleteObject(RedisKey.JD_ADVERTISE_LIST);
      } else {
        LOG.error("Logic update advertise error !");
        return SaResult.error("更新失败");
      }
      return SaResult.ok("更新成功");
    } catch (Exception e) {
      ExceptionUtil.exceptionToString(e);
      e.printStackTrace();
      LOG.error("Logic update advertise error !", e.getMessage());
      return SaResult.error("广告更新失败！服务器异常！");
    }
  }
}
