package com.spaceobj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author zhr_java@163.com
 * @date 2022/7/27 22:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Other implements Serializable {
    /**
     * 客服微信
     */
    @NotBlank(message = "客服微信不为空")
    private String wechat;

    /**
     * 下载链接
     */
    @NotBlank(message = "下载链接不为空")
    private String downloadUrl;

}
