package com.bjf.mapper;

import com.bjf.pojo.BjfMdiscp;

import java.util.List;

public interface BjfMdiscpMapper {
    List<BjfMdiscp> selectAll();//查询所有
    BjfMdiscp selectOne(Integer id);//查询单张优惠券
    Integer updateNum(Integer mdpId);//修改优惠券数量
}