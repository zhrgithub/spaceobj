package com.spaceobj.other.pojo;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:04
 */
@Data
@Builder
public class Other implements Serializable {

    public Other() {

    }

    public Other(String wechat, String downloadUrl) {

        this.wechat = wechat;
        this.downloadUrl = downloadUrl;
    }

    /**
     * 客服微信
     */
    @NotBlank
    private String wechat;

    /**
     * 下载链接
     */
    @NotBlank
    private String downloadUrl;

}
