package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * bjf_commodity_details
 * @author 
 */
@Data
public class BjfCommodityDetails implements Serializable {
    /**
     * 详情表id主键
     */
    private Integer cmaId;

    /**
     * 出厂厂家名
     */
    private String cmaName;

    /**
     * 厂家地址
     */
    private String cmaAddress;

    /**
     * 保质期
     */
    private String cmaDay;

    /**
     * 厂家联系电话
     */
    private String cmaPhone;

    /**
     * 品牌名称
     */
    private String cmaBrname;

    /**
     * 商品产地
     */
    private String cmaCadress;

    /**
     * 商品净重
     */
    private String cmaWeight;

    /**
     * 商品存储环境
     */
    private String cmaEnviro;

    /**
     * 商品id
     */
    private Integer cmdId;

    /**
     * 详情页商品图片
     */
    private String cmaImage;

    /**
     * 商品图片
     */
    private String cmaPicture;

    private static final long serialVersionUID = 1L;
}