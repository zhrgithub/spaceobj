package generate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * jd_advertis
 * @author 
 */
@Data
public class JdAdvertis implements Serializable {
    /**
     * 广告ID
     */
    private Long jdAdId;

    /**
     * 广告超链接
     */
    private String jdAdHyperlink;

    /**
     * 图片链接
     */
    private String jdAdImageLink;

    /**
     * 商品名称
     */
    private String jdAdName;

    /**
     * 商店名称
     */
    private String jdAdStoreName;

    /**
     * 优惠券描述
     */
    private String jdAdCoupon;

    /**
     * 价格描述
     */
    private BigDecimal jdAdPrice;

    /**
     * 好评数量
     */
    private String jdAdCommentNum;

    /**
     * 创建时间
     */
    private Date jdAdCreateTime;

    /**
     * 修改时间
     */
    private Date jdAdUpdateTime;

    private static final long serialVersionUID = 1L;
}