package com.bjf.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * bjf_backment
 * @author 
 */
@Data
public class BjfBackment implements Serializable {
    private Integer bmId;

    /**
     * 对外交易编号
     */
    private String payOutTradeNo;

    /**
     * 订单编号，支付宝生成
     */
    private String payAlipayTradeNo;

    /**
     * 订单详情表id
     */
    private Integer oiId;

    /**
     * 退款金额
     */
    private BigDecimal bmMoney;


    /**
     * 退款状态0未退 1已退
     */
    private Integer bmStatus;
    /**
     * 退款理由
     */
    private String bmReasion;
    /**
     * 退款图片
     */
    private String bmImage;
    /**
     * 申请退款时间
     */
    private Date bmBackTime;
    /**
     * 退款完成时间
     */
    private Date bmBackSuccessTime;

    private static final long serialVersionUID = 1L;
}