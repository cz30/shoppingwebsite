package com.bjf.mapper;

import com.bjf.pojo.BjfMcoupon;
import org.springframework.data.repository.query.Param;


public interface BjfMcouponMapper {
    Integer selectCouPon(@Param("uid") Integer uid,@Param("mdpId") Integer mdpId); //查询用户是否有该优惠券
    int insertCoupon(@Param("mpId") Integer mdpId, @Param("uid") Integer uid); //添加一条数据
    int deleteCoupon(Integer id);//删除数据
}