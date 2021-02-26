package com.bjf.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * bjf_merchant
 * @author 
 */
@Data
public class BjfMerchant implements Serializable {
    /**
     * 主键  
     */
    private Integer mcId;

    /**
     * 注册人姓名
     */
    private String mcName;

    /**
     * 商家logo
     */
    private String mcMessage;

    /**
     * 邮箱
     */
    private String mcEmail;

    /**
     * 手机号  
     */
    private String mcPhoneNumber;

    /**
     * 身份证号
     */
    private String mcIdCard;

    /**
     * 备案编码
     */
    private String mcCode;

    /**
     * 营业执照
     */
    private String mcLicense;

    /**
     * 账号
     */
    private String mcUsername;

    /**
     * 密码
     */
    private String mcPassword;

    /**
     * 密文
     */
    private String mcPwd;

    /**
     * 权限
     */
    private String mcPerms;

    /**
     * 地址
     */
    private String mcAddress;

    /**
     * 配送费
     */
    private BigDecimal mcDpfee;

    /**
     * 满多少免配送费
     */
    private BigDecimal mcCmdfee;

    /**
     * 自动确认收货时间/h
     */
    private Integer mcSuccessTime;

    private static final long serialVersionUID = 1L;
}