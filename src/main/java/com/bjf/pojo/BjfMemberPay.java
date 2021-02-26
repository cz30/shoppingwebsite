package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * bjf_member_pay
 * @author 
 */
@Data
public class BjfMemberPay implements Serializable {
    private Integer mbpId;

    /**
     * 月份
     */
    private Integer mbpMonth;

    /**
     * 金额
     */
    private Double mbpMoney;

    private static final long serialVersionUID = 1L;
}