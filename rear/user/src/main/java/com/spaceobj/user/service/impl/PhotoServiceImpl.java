package com.spaceobj.user.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.domain.SysPhoto;
import com.spaceobj.user.bo.SysPhotoBo;
import com.spaceobj.user.constant.OperationType;
import com.spaceobj.user.constant.RedisKey;
import com.spaceobj.user.mapper.SysPhotoMapper;
import com.spaceobj.user.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

  @Override
  public SaResult addOrUpdate(SysPhotoBo sysPhotoBo) {

    Integer result = -1;
    try {

      SysPhoto photo = SysPhoto.builder().build();
      BeanUtils.copyProperties(sysPhotoBo, photo);
      if (sysPhotoBo.getOperation().equals(OperationType.ADD)) {
        photo.setPhotoId(null);
        result = sysPhotoMapper.insert(photo);

        if (result == 1) {
          redisTemplate.delete(RedisKey.SYS_PHOTO_LIST);
          return SaResult.ok("新增成功");
        } else {
          return SaResult.error("数据新增失败");
        }

      } else if (sysPhotoBo.getOperation().equals(OperationType.UPDATE)) {

        // 判断修改的图片id是否为空
        if (sysPhotoBo.getPhotoId() == null) {
          return SaResult.error("图片id不为空");
        }
        result = sysPhotoMapper.updateById(photo);
        if (result == 1) {
          redisTemplate.delete(RedisKey.SYS_PHOTO_LIST);
        } else {
          return SaResult.error("数据更新失败");
        }
      } else {
        return SaResult.error("操作类型不正确");
      }

    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("Photo addOrUpdate failed", e.getMessage());
      return SaResult.error("数据操作失败，服务器异常");
    }
    return SaResult.ok("更新成功");
  }

  @Override
  public SaResult delete(SysPhotoBo sysPhotoBo) {
    Integer result = -1;
    try {
      if (sysPhotoBo.getPhotoId() == null) {
        return SaResult.error("请求参数错误");
      }
      result = sysPhotoMapper.deleteById(sysPhotoBo.getPhotoId());
      if (result == 1) {
        redisTemplate.delete(RedisKey.SYS_PHOTO_LIST);
      } else {
        return SaResult.error("删除失败");
      }
    } catch (RuntimeException e) {
      LOG.error("Photo delete failed", e.getMessage());
      return SaResult.error("删除失败");
    }
    return SaResult.ok("删除成功");
  }

  @Override
  public SaResult photoList() {

    List<SysPhoto> list = null;

    try {
      boolean size = redisTemplate.hasKey(RedisKey.SYS_PHOTO_LIST);
      if (!size) {
        // 如果是否在同步中，那么等待50ms后重新调用
        if (getRedisPhotoListSyncStatus()) {
          Thread.sleep(200);
          this.photoList();
        } else {
          redisTemplate.opsForValue().set(RedisKey.PHOTO_LIST_SYNC_STATUS, true);

          QueryWrapper<SysPhoto> queryWrapper = new QueryWrapper<>();
          list = sysPhotoMapper.selectList(queryWrapper);
          redisTemplate.opsForList().leftPushAll(RedisKey.SYS_PHOTO_LIST, list.toArray());

          redisTemplate.opsForValue().set(RedisKey.PHOTO_LIST_SYNC_STATUS, false);
        }

      } else {
        list = redisTemplate.opsForList().range(RedisKey.SYS_PHOTO_LIST, 0, -1);
      }
    } catch (RuntimeException | InterruptedException e) {
      LOG.error("Photo list failed", e.getMessage());
      return SaResult.error(e.getMessage()).setData(list);
    }
    return SaResult.ok().setData(list);
  }

  public boolean getRedisPhotoListSyncStatus() {
    boolean hasKey = redisTemplate.hasKey(RedisKey.PHOTO_LIST_SYNC_STATUS);
    if (!hasKey) {
      redisTemplate.opsForValue().set(RedisKey.PHOTO_LIST_SYNC_STATUS, false);
      return false;
    } else {
      return (boolean) redisTemplate.opsForValue().get(RedisKey.PHOTO_LIST_SYNC_STATUS);
    }
  }
}
