package com.spaceobj.advertise.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.advertise.bo.JdAdvertiseBo;
import com.spaceobj.advertise.pojo.JdAdvertise;

/**
 * @author zhr_java@163.com
 * @date 2022/7/18 14:49
 */
public interface JdAdvertiseService extends IService<JdAdvertise> {

    /**
     * 查询所有的广告内容
     *
     * @return
     */
    SaResult findList();

    /**
     * 新增广告内容
     *
     * @param jdAdvertiseBo
     *
     * @return
     */
    SaResult saveAdvertise(JdAdvertiseBo jdAdvertiseBo);

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
     * @param jdAdvertiseBo
     *
     * @return
     */
    SaResult updateAdvertise(JdAdvertiseBo jdAdvertiseBo);

}
