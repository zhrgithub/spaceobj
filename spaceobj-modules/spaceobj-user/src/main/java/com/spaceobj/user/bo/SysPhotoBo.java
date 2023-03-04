package com.spaceobj.user.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhr
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPhotoBo implements Serializable {

    /** 图片id */
    private String photoId;

    /** 图片URl */
    private String photoUrl;

    /** 操作类型 */
    private Integer operation;

}
