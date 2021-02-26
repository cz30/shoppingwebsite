package com.bjf.mapper;

import com.bjf.pojo.BjfCart;

import java.util.List;

public interface BjfCartMapper {
    int deleteByPrimaryKey(Integer cartId);

    int insert(BjfCart record);

    int insertSelective(BjfCart record);

    BjfCart selectByPrimaryKey(Integer cartId);

    int updateByPrimaryKeySelective(BjfCart record);

    int updateByPrimaryKey(BjfCart record);

    BjfCart findOneCart(Integer uId, Integer spfId);

    List<BjfCart> findByUId(Integer uId);
}