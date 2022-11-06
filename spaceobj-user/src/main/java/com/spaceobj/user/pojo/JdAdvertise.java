package com.spaceobj.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhr
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "jd_advertise")
@Builder
public class JdAdvertise implements Serializable {

  @TableId(value = "jd_ad_id")
  private Integer jdAdId;

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
  private BigDecimal jdAdPrice;

  @TableField(value = "jd_ad_comment_num")
  private String jdAdCommentNum;
}
