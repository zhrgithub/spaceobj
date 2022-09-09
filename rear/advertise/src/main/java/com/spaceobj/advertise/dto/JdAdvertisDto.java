package com.spaceobj.advertise.dto;

import com.sun.istack.internal.NotNull;
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
public class JdAdvertisDto implements Serializable {

    private Long jdAdId;

    @NotNull
    private String jdAdHyperlink;

    @NotNull
    private String jdAdImageLink;

    @NotNull
    private String jdAdName;

    @NotNull
    private String jdAdStoreName;

    @NotNull
    private String jdAdCoupon;

    @NotNull
    private BigDecimal jdAdPrice;

    @NotNull
    private String jdAdCommentNum;

}
