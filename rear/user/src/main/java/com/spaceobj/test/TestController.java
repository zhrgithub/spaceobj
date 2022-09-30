package com.spaceobj.test;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.user.pojo.SysUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据压缩测试
 *
 * @author zhr_java@163.com
 * @date 2022/9/18 13:55
 */
@RestController("TestController")
@RequestMapping(value = "/test", method = RequestMethod.POST)
public class TestController {

  @PostMapping("getJson")
  public SaResult getJson() {

    List<SysUser> userList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      SysUser sysUser =
          SysUser.builder().account("fsafsfsdadfsa").deviceType("fsfdsaddsfsfds").build();
      userList.add(sysUser);
    }

    return SaResult.ok().setData(userList);
  }

  @PostMapping("getString")
  public String getString() {
    List<SysUser> userList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      SysUser sysUser =
          SysUser.builder().account("fsafsfsdadfsa").deviceType("fsfdsaddsfsfds").build();
      userList.add(sysUser);
    }

    return SaResult.ok().setData(userList).toString();
  }
}
