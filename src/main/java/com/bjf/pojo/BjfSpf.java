package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * bjf_spf
 * @author 
 */
@Data
public class BjfSpf implements Serializable {
    /**
     * 主键id
     */
    private Integer spfId;

    /**
     * 规格内容
     */
    private String spfContent;

    /**
     * 库存
     */
    private Integer spfCount;

    /**
     * 商品id
     */
    private Integer cmdId;

    /**
     * 价格
     */
    private BigDecimal spfPrice;

    /**
     * 图片
     */
    private String spfImage;

    private static final long serialVersionUID = 1L;
}