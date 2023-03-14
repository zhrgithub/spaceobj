package com.spaceobj.project.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhr
 */
@Data
@Builder
@TableName(value = "sys_project")
@AllArgsConstructor
@NoArgsConstructor
public class SysProject implements Serializable {

    /**
     * 项目id
     */
    @TableId(value = "p_id")
    private long pId;

    /**
     * 项目新增时候的UUID
     */
    @TableField(value = "p_uuid")
    private String uuid;

    @TableField(value = "p_content")
    private String content;

    @TableField(value = "p_price")
    private BigDecimal price;

    @TableField(value = "p_release_user_id")
    private String releaseUserId;

    @TableField(value = "p_page_views")
    private long pageViews;

    @TableField(value = "p_status")
    private long status;

    @TableField(value = "p_ip_address")
    private String ipAddress;

    @TableField(value = "p_nick_name")
    private String nickname;

    /** 审核内容 */
    @TableField(value = "p_message")
    private String message;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(value = "version")
    private long version;

    /**
     * 是否代发
     */
    @TableField(value = "dropshipping")
    private String dropshipping;

    /**
     * 联系方式
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

}
