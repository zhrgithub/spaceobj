package com.spaceobj.other.service;

import cn.dev33.satoken.util.SaResult;
import com.spaceobj.other.pojo.Other;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:07
 */
public interface OtherService {

    /**
     * 修改内容
     *
     * @param other
     *
     * @return
     */
    SaResult updateOther(Other other);

    /**
     * 查询内容
     *
     * @return
     */
    SaResult getOther();

}
