package com.bjf.mapper;

import com.bjf.pojo.BjfCoupon;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BjfCouponMapper {
    //添加一条数据
    int insertConpon(BjfCoupon bjfCoupon);
    //删除优惠券
    Integer deleteCoupon(Integer cnId);

    int deleteByPrimaryKey(Integer cnId);


    int insert(BjfCoupon record);

    int insertSelective(BjfCoupon record);

    BjfCoupon selectByPrimaryKey(Integer cnId);

    //根据用户id获取所有优惠券
    List<BjfCoupon> selectByUid(Integer uId);

    int updateByPrimaryKeySelective(BjfCoupon record);

    int updateByPrimaryKey(BjfCoupon record);
}