package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * bjf_mdiscp
 * @author 
 */
@Data
public class BjfMdiscp implements Serializable {
    private Integer mdpId;

    /**
     * 优惠券名称
     */
    private String mdpName;

    /**
     * 优惠券的值
     */
    private Integer mdpValue;

    /**
     * 优惠券最低使用价值
     */
    private Integer mdpLowValue;

    /**
     * 优惠券开始使用时间
     */
    private Date mdpStartime;

    /**
     * 优惠券到期时间
     */
    private Date mdpEndtime;

    /**
     * 优惠券类型0通用 1满减
     */
    private Integer mdpType;

    /**
     * 优惠券数量
     */
    private Integer mdpNum;

    /**
     * 是否可领取发放 0可1不可
     */
    private Integer mdpSwitch;
    /**
     * 天数
     */
    private Integer time;

    private static final long serialVersionUID = 1L;
}