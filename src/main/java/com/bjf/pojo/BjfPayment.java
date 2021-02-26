package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * bjf_payment
 * @author 
 */
@Data
public class BjfPayment implements Serializable {
    /**
     * 当前表id
     */
    private Integer payId;

    /**
     * 对外交易编号
     */
    private String payOutTradeNo;

    /**
     * 订单编号，支付宝生成
     */
    private String payAlipayTradeNo;

    /**
     * 订单总额
     */
    private BigDecimal payTotalAmount;

    /**
     * 交易内容，利用商品名称拼接
     */
    private String paySubject;

    /**
     * 支付状态，默认未支付
     */
    private Integer payPaymentStatus;

    /**
     * 创建时间
     */
    private Date payCreateTime;

    /**
     * 回调时间，初始为空，支付宝异步回调时记录
     */
    private Date payCallbackTime;

    /**
     * 回调信息，初始为空，支付宝异步回调时记录
     */
    private String payCallbackContent;


    /**
     * 支付方式：0支付宝 1微信
     */
    private Integer payWay;

    private BigDecimal payOutMoney;

    private static final long serialVersionUID = 1L;
}