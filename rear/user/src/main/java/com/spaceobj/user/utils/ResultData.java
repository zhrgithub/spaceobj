package com.spaceobj.user.utils;

import com.spaceobj.user.constent.RestCode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhr_java@163.com
 * @date 2022/07/23 22:00
 */
@Data
public class ResultData {

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private Map<String, Object> data = new HashMap<>();

    public static ResultData success() {

        ResultData r = new ResultData();
        r.code = RestCode.SUCCESS;
        r.message = "当前请求操作成功";
        return r;
    }

    public static ResultData success(String message) {

        ResultData r = new ResultData();
        r.code = RestCode.SUCCESS;
        r.message = message;
        return r;
    }

    public static ResultData unAuthorize() {

        ResultData r = new ResultData();
        r.code = RestCode.UN_AUTHORIZE;
        r.message = "未登录";
        return r;
    }

    public static ResultData error() {

        ResultData r = new ResultData();
        r.code = RestCode.ERROR;
        r.message = "当前请求操作失败，稍后重试";
        return r;
    }

    public static ResultData error(String message) {

        ResultData r = new ResultData();
        r.code = RestCode.ERROR;
        r.message = message;
        return r;
    }

    public ResultData setMessage(String message) {

        this.message = message;
        return this;
    }

    public ResultData setData(String key, Object value) {

        this.data.put(key, value);
        return this;
    }


    public static ResultData busy(){
        ResultData result = new ResultData();
        result.code = RestCode.busy;
        result.message = "服务器繁忙";
        return result;
    }

    public static ResultData frequently(){
        ResultData result = new ResultData();
        result.code = RestCode.frequently;
        result.message = "操作频繁，请稍后重试";
        return result;
    }

}
