package com.bjf.service;

import com.bjf.pojo.BjfCoupon;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BjfCouponService {
    List showAllCouPon(); //展示所有的优惠券
    int  addCoupon(Integer uid,Integer mdpId);//领取优惠券

    //删除优惠券
    Integer deleteCoupon(Integer cnId);

    //    g根据用户id 获取有优惠券
    List<BjfCoupon> selectByUid(Integer uId);

    BjfCoupon getById(Integer cnId);

    void changeCoupon(BjfCoupon coupon);

}
