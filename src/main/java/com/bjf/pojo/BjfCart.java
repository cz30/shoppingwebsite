package com.bjf.pojo;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * bjf_cart
 * @author 
 */
@Data
public class BjfCart implements Serializable {
    /**
     * 主键
     */
    private Integer cartId;

    /**
     * 图片
     */
    private String cartImage;

    /**
     * 单个商品实际价格
     */
    private BigDecimal cartOprice;

    /**
     * 小计
     */
    private BigDecimal cartNprice;

    /**
     * 名称
     */
    private String cartTitle;

    /**
     * 数量
     */
    private Integer cartNum;

    /**
     * 连接用户id
     */
    private Integer uId;

    /**
     * 连接商品id
     */
    private Integer cmdId;

    /**
     * 添加时间
     */
    private Date cartTime;

    /**
     * 规格表id
     */
    private Integer spfId;

    /**
     * 规格内容
     */
    private String cartContent;

    /**
     * 规格内容
     */
    @Transient
    private Integer spfCount;

    /**
     * 对应商品的折扣
     */
    @Transient
    private BigDecimal discount;

    public void sumPrice() {
        BigDecimal price;
        price = getCartOprice().multiply(BigDecimal.valueOf(getCartNum()));
        this.cartNprice = price;
    }

    private static final long serialVersionUID = 1L;
}