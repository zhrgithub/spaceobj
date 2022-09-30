package com.spaceobj.email.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spaceobj.email.mapper.SysEmailMapper;
import com.spaceobj.email.pojo.SysEmail;
import com.spaceobj.email.service.SysEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Service
public class SysEmailServiceImpl extends ServiceImpl<SysEmailMapper, SysEmail>
    implements SysEmailService {

  @Autowired
  private SysEmailMapper sysEmailMapper;

  @Autowired
  private RedisTemplate redisTemplate;

  /** 系统邮箱列表 */
  private static final String SYS_EMAIL_LIST = "sys_email_list";

  private static final Logger LOG = LoggerFactory.getLogger(SysEmailServiceImpl.class);

  @Override
  public SaResult findList() {

    List<SysEmail> list = null;
    try {
      Long size = redisTemplate.opsForList().size(SYS_EMAIL_LIST);
      if (size == 0) {
        QueryWrapper queryWrapper = new QueryWrapper();
        list = sysEmailMapper.selectList(queryWrapper);
        redisTemplate.opsForList().rightPushAll(SYS_EMAIL_LIST, list.toArray());
      } else {
        // 可以使用pipeLine来提升性能
        list = redisTemplate.opsForList().range(SYS_EMAIL_LIST, 0, -1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("查询邮箱数据异常");
      return SaResult.error("发件箱列表异常").setData(list);
    }
    return SaResult.ok().setData(list);
  }
}
