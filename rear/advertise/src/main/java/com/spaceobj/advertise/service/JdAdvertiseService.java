package com.spaceobj.advertise.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.advertise.pojo.JdAdvertis;

/**
 * @author zhr_java@163.com
 * @date 2022/7/18 14:49
 */
public interface JdAdvertiseService extends IService<JdAdvertis> {

    /**
     * 查询所有的广告内容
     *
     * @return
     */
    SaResult findList();

    /**
     * 新增广告内容
     *
     * @param jdAdvertis
     *
     * @return
     */
    SaResult saveAdvertise(JdAdvertis jdAdvertis);

    /**
     * 删除广告内容
     *
     * @param id
     *
     * @return
     */
    SaResult deleteAdvertise(String id);

    /**
     * 修改广告内容
     *
     * @param jdAdvertis
     *
     * @return
     */
    SaResult updateAdvertise(JdAdvertis jdAdvertis);

}
