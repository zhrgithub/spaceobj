package com.spaceobj.gateway.component;

/**
 * @author zhr_java@163.com
 * @date 2022/9/11 17:27
 */
import cn.dev33.satoken.stp.StpInterface;
import com.spaceobj.common.core.dto.SysUser;
import com.spaceobj.common.core.dto.UserPermission;
import com.spaceobj.common.core.utils.RsaUtils;
import com.spaceobj.common.redis.constant.RedisKey;
import com.spaceobj.common.redis.service.RedisService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
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

  @Resource private UserClient userClient;

  @Autowired private RedisService redisService;

  @Value("${privateKey}")
  private String privateKey;

  /** 返回一个账号所拥有的权限码集合 */
  @SneakyThrows
  @Override
  public List<String> getPermissionList(Object loginId, String loginType) {

    // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
    List<String> list = new ArrayList<>();
    try {
      UserPermission userPermission = getUserPermission((String) loginId);
      if (ObjectUtils.isEmpty(userPermission)) {
        return list;
      }

      String[] userRights = userPermission.getUserRights().split(",");
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
    List<String> list = new ArrayList<>();
    return list;
  }

  /**
   * 根据账户获取用户权限，异常则返回null
   *
   * @param account
   * @return
   * @throws InterruptedException
   */
  public UserPermission getUserPermission(String account) {
    UserPermission userPermission = null;
    try {
      // 先到Redis中取一下
      boolean flag = redisService.HExists(RedisKey.SYS_USER_LIST, account);
      if (flag) {
        SysUser sysUser =
            redisService.getCacheMapValue(RedisKey.SYS_USER_LIST, account, SysUser.class);
        userPermission = new UserPermission(sysUser.getUserType(),sysUser.getUserRights());
        return userPermission;
      }
      // 缓存中没有，那么调用远程服务
      Object res = userClient.getUserPermissionByAccount(account);
      userPermission = RsaUtils.decryptByPrivateKey(res, UserPermission.class, privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return userPermission;
  }
}
