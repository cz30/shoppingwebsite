package com.bjf.pojo.vo;

import com.bjf.pojo.BjfOrderItem;
import com.bjf.pojo.BjfPayment;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * bjf_order
 * @author 
 */
@Data
public class BjfOrderVo implements Serializable {
    /**
     * 当前表id
     */
    private Integer odId;

    /**
     * 归属用户的id
     */
    private Integer uId;

    /**
     * 收货人
     */
    private String odRecvName;

    /**
     * 收货人电话
     */
    private String odRevcPhone;

    /**
     * 订单总额
     */
    private BigDecimal odTotalAmount;

    /**
     * 订单状态:0-未支付，1-已支付，2-已取消，3-已关闭，4-已完成
     */
    private Integer odStatus;

    /**
     * 第三方支付编号，按规则生成
     */
    private String odOutTradeNo;

    /**
     * 下单时间
     */
    private Date odTime;
    //下单时间String
    private String odTimeStr;

    /**
     * 过期时间，默认当前时间+1天
     */
    private Date odExpireTime;
    private String odExpireTimeStr;
    /**
     * 支付时间
     */
    private Date odPayTime;
    private String odPayTimeStr;

    /**
     * 订单编号
     */
    private String odDelid;

    /**
     * 订单完成时间
     */
    private Date odModifiedTime;
    private String odModifiedTimeStr;

    /**
     * 收货地址
     */
    private String odRecvAddress;

    /**
     * 退款状态：0、退款成功，1、未退款，2、退款失败
     */
    private Integer odRefundStatus;

    /**
     * 买家留言
     */
    private String odMessage;


    private String odReasion;
    private String odReimage;

    private String odSendName;//配送人名称
    private String odSendPhone;//配送人电话
    private BigDecimal mcDpfee;//配送费
    private Integer cnValue;//优惠券价值


    private List<BjfOrderItem> bjfOrderItems;
    private BjfPayment bjfPayment;


    private static final long serialVersionUID = 1L;
}