package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * bjf_address
 * @author 
 */
@Data
public class BjfAddress implements Serializable {
    /**
     * id主键
     */
    private Integer adId;

    /**
     * 地址
     */
    private String adAddress;

    /**
     * 用户id
     */
    private Integer uId;

    /**
     * 收货人
     */
    private String adUser;

    /**
     * 收件人电话
     */
    private String adPhone;

    private static final long serialVersionUID = 1L;
}