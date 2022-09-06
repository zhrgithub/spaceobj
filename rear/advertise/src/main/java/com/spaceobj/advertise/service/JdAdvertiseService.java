package com.spaceobj.advertise.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.advertise.bo.JdAdvertisBo;
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
     * @param jdAdvertisBo
     *
     * @return
     */
    SaResult saveAdvertise(JdAdvertisBo jdAdvertisBo);

    /**
     * 删除广告内容
     *
     * @param id
     *
     * @return
     */
    SaResult deleteAdvertise(long id);

    /**
     * 修改广告内容
     *
     * @param jdAdvertisBo
     *
     * @return
     */
    SaResult updateAdvertise(JdAdvertisBo jdAdvertisBo);

}
