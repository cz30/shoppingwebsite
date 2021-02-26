package com.bjf.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * bjf_coupon
 * @author 
 */
@Data
public class BjfCoupon implements Serializable {
    /**
     * id主键
     */
    private Integer cnId;

    /**
     * 优惠券名称
     */
    private String cnName;

    /**
     * 使用条件
     */
    private Integer cnDetal;

    /**
     * 使用时间
     */
    private Date cnUsetime;

    /**
     * 到期时间
     */
    private Date cnExpire;

    /**
     * 优惠券价值
     */
    private Integer cnValue;

    /**
     * 最低使用值
     */
    private Integer cnLowValue;

    /**
     * 使用状态：0待使用，1已过期
     */
    private Integer cnStatus;

    /**
     * 用户id
     */
    private Integer uId;



    private static final long serialVersionUID = 1L;
}