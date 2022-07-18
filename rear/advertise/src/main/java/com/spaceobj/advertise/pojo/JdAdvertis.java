package com.spaceobj.advertise.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "jd_advertis")
public class JdAdvertis implements Serializable {

    @TableField(value = "jd_ad_Id")
    private long jdAdId;

    @TableField(value = "jd_ad_hyperlink")
    private String jdAdHyperlink;

    @TableField(value = "jd_ad_image_link")
    private String jdAdImageLink;

    @TableField(value = "jd_ad_name")
    private String jdAdName;

    @TableField(value = "jd_ad_store_name")
    private String jdAdStoreName;

    @TableField(value = "jd_ad_coupon")
    private String jdAdCoupon;

    @TableField(value = "jd_ad_price")
    private double jdAdPrice;

    @TableField(value = "jd_ad_comment_num")
    private String jdAdCommentNum;
}
