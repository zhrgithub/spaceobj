package com.spaceobj.constant;

/**
 * @author zhr_java@163.com
 * @date 2022/9/11 17:27
 */
import cn.dev33.satoken.stp.StpInterface;
import com.spaceobj.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 *
 * @author zhr
 */
@Component
public class StpInterfaceImpl implements StpInterface {

  @Autowired private RedisTemplate redisTemplate;

  /** 返回一个账号所拥有的权限码集合 */
  @Override
  public List<String> getPermissionList(Object loginId, String loginType) {

    // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
    List<String> list = new ArrayList<String>();

    SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(loginId);
    String[] userRights = sysUser.getUserRights().split(",");
    if (userRights.length > 0) {
      Arrays.stream(userRights)
          .forEach(
              ur -> {
                list.add(ur);
              });
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
}
