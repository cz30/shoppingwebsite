package com.bjf.pojo;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * bjf_commodity
 * @author 
 */
@Data
public class BjfCommodity implements Serializable {
    /**
     * 商品id
     */
    private Integer cmdId;

    /**
     * 商品名
     */
    private String cmdName;

    /**
     * 商品图片
     */
    private String cmdImage;

    /**
     * 具体商品最低价
     */
    private BigDecimal cmdPrice;

    /**
     * 类目id
     */
    private Integer cgId;

    /**
     * 评价表id
     */
    private Integer elId;

    /**
     * 是否热销（0/1）状态
     */
    private Integer cmdHot;

    /**
     * 销量
     */
    private Integer cmdCake;

    /**
     * 是否推荐（0/1）状态
     */
    private Integer cmdRecommend;

    /**
     * 商品赠送积分
     */
    private Integer cmdScore;

    /**
     * 是否参与会员打折
     */
    private Integer cmdCountStatus;

    /**
     * 会员折扣
     */
    private BigDecimal cmdDiscount;

    /**
     * 是否支持退款0不支持1支持
     */
    private Integer cmdSupport;

    @Transient
    private BjfCommodityDetails bjfCommodityDetails;

    @Transient
    private List<BjfSpf> bjfSpfList;

    private static final long serialVersionUID = 1L;
}