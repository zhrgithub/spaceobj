package com.spaceobj.advertise.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JdAdvertisBo implements Serializable {

    private Long jdAdId;

    private String jdAdHyperlink;

    private String jdAdImageLink;

    private String jdAdName;

    private String jdAdStoreName;

    private String jdAdCoupon;

    private BigDecimal jdAdPrice;

    private String jdAdCommentNum;

}
