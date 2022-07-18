package com.spaceobj.advertise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spaceobj.advertise.pojo.JdAdvertis;
import com.spaceobj.advertise.utils.ResultData;

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
    ResultData findList();

    /**
     * 新增广告内容
     *
     * @param jdAdvertis
     *
     * @return
     */
    ResultData saveAdvertis(JdAdvertis jdAdvertis);

    /**
     * 删除广告内容
     *
     * @param id
     *
     * @return
     */
    ResultData deleteAdvertis(String id);

    /**
     * 修改广告内容
     *
     * @param jdAdvertis
     *
     * @return
     */
    ResultData updateAdvertis(JdAdvertis jdAdvertis);

}
