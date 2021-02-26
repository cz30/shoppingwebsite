package com.bjf.service;


import com.bjf.pojo.BjfMdiscp;
import com.bjf.pojo.vo.BjfMdiscpVo;

import java.util.List;

public interface BjfMdiscpService {
    List<BjfMdiscpVo> showAll(Integer id);//展示所有可领取的优惠券
    Integer insertData(Integer uid,Integer mdpId);//用户领取一张优惠券
    Integer newUserGet(Integer uid, Integer mdpId);//新用户领取优惠券
    Integer insertIntoCoupon(BjfMdiscp bjfMdiscp,Integer mdpId,Integer uid);//插入优惠券
    Integer userLoginGet(Integer uid,Integer mdpId);//用户登录领取优惠券
}
