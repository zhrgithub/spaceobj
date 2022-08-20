package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.user.constent.OperationType;
import com.spaceobj.user.mapper.SysPhotoMapper;
import com.spaceobj.user.pojo.SysPhoto;
import com.spaceobj.user.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 00:28
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<SysPhotoMapper, SysPhoto>
    implements PhotoService {
  private static final Logger LOG = LoggerFactory.getLogger(PhotoServiceImpl.class);

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private SysPhotoMapper sysPhotoMapper;

  public static final String SYS_PHOTO_LIST = "sys_photo_list";

  @Override
  public SaResult addOrUpdate(SysPhoto photo, Integer operation) {

    Integer result = -1;
    try {

      if (operation.equals(OperationType.ADD)) {
        result = sysPhotoMapper.insert(photo);
      } else {
        result = sysPhotoMapper.updateById(photo);
      }
      if (result == 1) {
        redisTemplate.delete(SYS_PHOTO_LIST);
        LOG.info("Photo addOrUpdate successfully");
      }
    } catch (Exception e) {
      LOG.error("Photo addOrUpdate failed", e.getMessage());
      return SaResult.error(e.getMessage()).setData(result);
    }
    return SaResult.ok().setData(result);
  }

  @Override
  public SaResult delete(Long id) {
    Integer result = -1;
    try {
      result = sysPhotoMapper.deleteById(id);
      redisTemplate.delete(SYS_PHOTO_LIST);
    } catch (RuntimeException e) {
      LOG.error("Photo delete failed", e.getMessage());
      return SaResult.error(e.getMessage()).setData(result);
    }
    return SaResult.ok().setData(result);
  }

  @Override
  public SaResult photoList() {

    List<SysPhoto> list = null;

    try {
      Long size = redisTemplate.opsForList().size(SYS_PHOTO_LIST);
      if (size == 0) {
        QueryWrapper<SysPhoto> queryWrapper = new QueryWrapper<>();
        synchronized (this) {
          list = sysPhotoMapper.selectList(queryWrapper);
          redisTemplate.opsForList().leftPushAll(SYS_PHOTO_LIST, list.toArray());
        }
      } else {
        list = redisTemplate.opsForList().range(SYS_PHOTO_LIST, 0, -1);
      }
    } catch (RuntimeException e) {
      LOG.error("Photo list failed", e.getMessage());
      return SaResult.error(e.getMessage()).setData(list);
    }
    return SaResult.ok().setData(list);
  }
}
