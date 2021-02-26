package com.bjf.pojo.vo;

import com.bjf.pojo.BjfCommodity;
import com.bjf.pojo.BjfMenberRights;
import lombok.Data;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/13
 * \* Time: 11:13
 * \* Description:
 * \
 */
@Data
public class VipHomePage {
    //会员表中是否有这个用户的会员信息（1/0）如果这个值为null，则代表用户没有登录，让用户去登录
    private Integer memberRecord;

    //判断用户是否过期了（1/0）
    private Integer memberState;

    private String uAccount;

    private String uHeadSculpture;

    //会员结束时间
    private String mbEtime;

    //剩余免费配送次数
    private Integer mbTimes;

    //所有的会员权益
    List<BjfMenberRights> bjfMenberRightsList;

    //部分享受折扣的商品
    List<BjfCommodity> bjfCommodityList;
}
