package com.spaceobj.advertise.dto;

import com.spaceobj.advertise.group.DeleteAdvertiseGroup;
import com.spaceobj.advertise.group.InsertAdverTiseGroup;
import com.spaceobj.advertise.group.UpdateAdvertiseGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JdAdvertisDto implements Serializable {

    @NotNull(message = "广告id不为空",groups = {DeleteAdvertiseGroup.class , UpdateAdvertiseGroup.class})
    private Integer jdAdId;

    @NotBlank(message = "广告超链接不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private String jdAdHyperlink;

    @NotBlank(message = "图片超链接不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private String jdAdImageLink;

    @NotBlank(message = "广告名称不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private String jdAdName;

    @NotBlank(message = "店铺名不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private String jdAdStoreName;

    @NotBlank(message = "优惠券描述不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private String jdAdCoupon;

    @NotNull(message = "价格不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private BigDecimal jdAdPrice;

    @NotBlank(message = "好评数量不为空",groups = {InsertAdverTiseGroup.class , UpdateAdvertiseGroup.class})
    private String jdAdCommentNum;

}
