package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * bjf_user
 * @author 
 */
@Data
public class BjfUser implements Serializable {
    /**
     * 主键
     */
    private Integer uId;

    /**
     * 账号
     */
    private String uUsername;

    /**
     * 密码
     */
    private String uPassword;

    /**
     * 加密后的密码
     */
    private String uPwd;

    /**
     * 权限
     */
    private String perms;

    /**
     * 邮箱
     */
    private String uEmail;

    /**
     * 手机号
     */
    private String uPhoneNumber;

    /**
     * 头像
     */
    private String uHeadSculpture;

    /**
     * 生日
     */
    private Date uBirth;



    /**
     * 积分
     */
    private Integer uIntegral;

    /**
     * 是否为svip
     */
    private Integer uMember;



    /**
     * 订单id
     */
    private String odId;

    /**
     * 昵称
     */
    private String uAccount;

    /**
     * 性别：男/女 0/1
     */
    private String uSex;

    /**
     * 默认地址id
     */
    private Integer adId;

    private static final long serialVersionUID = 1L;
}