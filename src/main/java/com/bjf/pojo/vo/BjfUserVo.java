package com.bjf.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * bjf_user
 * @author
 *
 * 添加 新字段SuBirth  string类型生日
 */
@Data
public class BjfUserVo implements Serializable {
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

//    字符串类型生日
    private String SuBirth;
    /**
     * 购物车id
     */
    private Integer cartId;

    /**
     * 积分
     */
    private Integer uIntegral;

    /**
     * 是否为svip  需要根据id 查询会员表 插入数据
     */
    private Integer uMember;

    /**
     * 消费量
     */
    private BigDecimal uSum;

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