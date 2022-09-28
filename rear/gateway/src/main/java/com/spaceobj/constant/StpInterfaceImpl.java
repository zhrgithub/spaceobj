package com.spaceobj.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/9/11 17:27
 */
import cn.dev33.satoken.stp.StpInterface;
import com.spaceobj.domain.SysUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限验证接口扩展
 *
 * @author zhr
 */
@Component
public class StpInterfaceImpl implements StpInterface {

  @Autowired private KafkaSender kafkaSender;

  @Autowired private RedisTemplate redisTemplate;


  /** 返回一个账号所拥有的权限码集合 */
  @SneakyThrows
  @Override
  public List<String> getPermissionList(Object loginId, String loginType) {

    // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
    List<String> list = new ArrayList<String>();
    try {
      SysUser sysUser = this.getSysUser(loginId.toString());
      String[] userRights = sysUser.getUserRights().split(",");
      if (userRights.length > 0) {
        Arrays.stream(userRights)
            .forEach(
                ur -> {
                  list.add(ur);
                });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }

  /** 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验) */
  @Override
  public List<String> getRoleList(Object loginId, String loginType) {
    // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
    List<String> list = new ArrayList<String>();
    return list;
  }

  /**
   * 根据账户获取用户信息
   *
   * @param account
   * @return
   * @throws InterruptedException
   */
  public SysUser getSysUser(String account) {
    SysUser sysUser = null;
    try {
      boolean flag = redisTemplate.hasKey(RedisKey.SYS_USER_LIST);
      List<SysUser> sysUserList = null;
      if (!flag) {
        // 刷新用户缓存信息
        kafkaSender.send(new Object(), KafKaTopics.UPDATE_USER_LIST);
        Thread.sleep(50);
        // 返回递归后的结果
        return this.getSysUser(account);
      } else {
        sysUserList = redisTemplate.opsForList().range(RedisKey.SYS_USER_LIST, 0, -1);
        sysUser =
            sysUserList.stream()
                .filter(
                    user -> {
                      return user.getAccount().equals(account);
                    })
                .collect(Collectors.toList())
                .get(0);
        return sysUser;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }
}
