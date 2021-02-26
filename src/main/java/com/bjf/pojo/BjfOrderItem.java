package com.bjf.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * bjf_order_item
 * @author 
 */
@Data
public class BjfOrderItem implements Serializable {
    /**
     * 当前表id
     */
    private Integer oiId;

    /**
     * 订单编号
     */
    private String odDelid;

    /**
     * 商品id
     */
    private Integer cmdId;

    /**
     * 商品名称
     */
    private String oiName;

    /**
     * 商品图片
     */
    private String oiImage;

    /**
     * 商品价格
     */
    private BigDecimal oiPrice;

    /**
     * 件数
     */
    private Integer oiNum;

    /**
     * 规格内容
     */
    private String oiContent;

    /**
     * 规格id
     */
    private Integer spfId;

    /**
     * 是否支持退款0不 1可
     */
    private Integer oiSupport;


    private String outTime;//过期时间

    /**
     * 商品退款状态
     */
    private Integer oiStatus;

    /**
     * 商品退货到期时间
     */
    private String oiTime;
    /**
     * 退款金额
     */
    private BigDecimal backMoney;

    /**
     * 会员价
     */
    @Transient
    private BigDecimal svipPrice;

    private static final long serialVersionUID = 1L;
}