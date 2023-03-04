package com.spaceobj.user.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.user.pojo.SysEmail;

/**
 * @author zhr_java@163.com
 * @date 2022/07/27 11:00
 */
public interface SysEmailService extends IService<SysEmail> {

    /**
     * 查询所有邮箱
     *
     * @return
     */
    SaResult findList();

}
