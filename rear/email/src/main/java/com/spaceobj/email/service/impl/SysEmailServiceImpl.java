package com.spaceobj.email.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.common.service.RedisService;
import com.spaceobj.email.mapper.SysEmailMapper;
import com.spaceobj.email.pojo.SysEmail;
import com.spaceobj.email.service.SysEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysEmailServiceImpl extends ServiceImpl<SysEmailMapper, SysEmail>
    implements SysEmailService {

  @Autowired private SysEmailMapper sysEmailMapper;

  @Autowired private RedisService redisService;

  /** 系统邮箱列表 */
  private static final String SYS_EMAIL_LIST = "sys_email_list";

  private static final Logger LOG = LoggerFactory.getLogger(SysEmailServiceImpl.class);

  @Override
  public SaResult findList() {

    List<SysEmail> list = null;
    try {
      boolean flag = false;
      flag = redisService.hasKey(SYS_EMAIL_LIST);
      if (!flag) {
        QueryWrapper<SysEmail> queryWrapper = new QueryWrapper();
        list = sysEmailMapper.selectList(queryWrapper);
        redisService.setCacheList(SYS_EMAIL_LIST, list);
      } else {
        // 可以使用pipeLine来提升性能
        list = redisService.getCacheList(SYS_EMAIL_LIST, SysEmail.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("查询邮箱数据异常");
      return SaResult.error("发件箱列表异常").setData(list);
    }
    return SaResult.ok().setData(list);
  }
}
