package com.spaceobj.component;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2022/10/22 14:39
 */
@Component
public class HttpMessageConvertersComponent {

    @Bean
    public HttpMessageConverters httpMessageConverter() {
        return  new HttpMessageConverters();
    }
}
