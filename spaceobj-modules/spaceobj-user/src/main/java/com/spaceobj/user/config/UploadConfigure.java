package com.spaceobj.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhr_java@163.com
 * @date 2023/3/4 20:22
 */
@Component
public class UploadConfigure {

    /**
     * 文件上传路径
     */
    @Value("${upload.path}")
    public String uploadPath;

    /**
     * 资源访问路径
     */
    @Value("${upload.url}")
    public String uploadUrl;

}
