package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * bjf_mcoupon
 * @author 
 */
@Data
public class BjfMcoupon implements Serializable {
    private Integer mcpId;

    /**
     * 商家优惠券id
     */
    private Integer mdpId;

    /**
     * 用户id
     */
    private Integer uId;

    private static final long serialVersionUID = 1L;
}