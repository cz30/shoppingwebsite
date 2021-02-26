package com.bjf.pojo.vo;

import com.bjf.pojo.BjfAddress;
import com.bjf.pojo.BjfCart;
import com.bjf.pojo.BjfCoupon;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderVO {

    List<BjfCart> cartList;

    List<BjfAddress> addressList;

    List<BjfCoupon> couponList;

    Integer integral;

    Integer defaultAdId;

    Boolean isMember;

    Integer MPTimers;

    BigDecimal dpFee;

    BigDecimal cmdFee;
}
