package com.spaceobj.advertise.constent;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhr_java@163.com
 * @date 2022/7/26 22:05
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常拦截
     *
     * @param e
     *
     * @return
     */
    @ExceptionHandler
    public SaResult handlerException(Exception e) {

        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }

}

