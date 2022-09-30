package com.spaceobj.advertise.bo;

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
@Builder
public class JdAdvertiseBo implements Serializable {

    private Integer jdAdId;

    private String jdAdHyperlink;

    private String jdAdImageLink;

    private String jdAdName;

    private String jdAdStoreName;

    private String jdAdCoupon;

    private BigDecimal jdAdPrice;

    private String jdAdCommentNum;

}
