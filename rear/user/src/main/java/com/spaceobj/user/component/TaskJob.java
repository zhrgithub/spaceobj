package com.spaceobj.user.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spaceobj.user.constent.RedisKey;
import com.spaceobj.user.mapper.SysUserMapper;
import com.spaceobj.user.pojo.SysUser;
import com.spaceobj.user.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhr_java@163.com
 * @date 2022/8/31 22:17
 */
@Component("taskJob")
public class TaskJob {

    Logger LOG = LoggerFactory.getLogger(TaskJob.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 每天凌晨触发，每日凌晨将所有用户的发布次数初始化成10，助力次数初始化成10，验证码发送次数3,创建项目助力链接的限度设置成10
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void job1() {
        try{
            // SysUser sysUser = null;
            // QueryWrapper<SysUser> sysQueryWrapper = new QueryWrapper<>();
            // sysQueryWrapper.eq("");
            // sysUserMapper.
            // List<SysUser> sysUserList = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST,0,-1);
            // for()
            // sysUserService.saveOrUpdateBatch();
        }catch (Exception e){
            LOG.error("job of update user info failed");
        }

    }


    /**
     * 每月一号触发，每月一号将用户信息的修改次数初始化成3
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void job2() {
        System.out.println("通过cron定义的定时任务");
    }
}
