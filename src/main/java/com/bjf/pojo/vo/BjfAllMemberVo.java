package com.bjf.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/9/4
 * \* Time: 9:04
 * \* Description:
 * \
 */
@Data
public class BjfAllMemberVo implements Serializable {
    /**
     * 主键
     */
    private Integer mbId;

    /**
     * 用户id
     */
    private Integer uId;

    private Integer uMember;

    /**
     * 会员状态
     */
    //private Integer mbStatus;
    private String uAccount;
    private String uHeadSculpture;
    /**
     * 会员开始
     */
    private Date mbStime;

    /**
     * 会员结束
     */
    private Date mbEtime;

    /**
     * 会员剩余免费配送次数
     */
    private Integer mbTimes;

    private static final long serialVersionUID = 1L;
}
