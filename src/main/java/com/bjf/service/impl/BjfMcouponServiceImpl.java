package com.bjf.service.impl;

import com.bjf.mapper.BjfMcouponMapper;
import com.bjf.service.BjfMcouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BjfMcouponServiceImpl implements BjfMcouponService {
    @Resource
    private BjfMcouponMapper bjfMcouponMapper;
    /**
     * 删除数据
     * @param id 商家优惠券id
     * @return
     */
    @Override
    public Integer deleteCoupon(Integer id) {
        return bjfMcouponMapper.deleteCoupon(id);
    }
}
