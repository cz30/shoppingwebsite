package com.bjf.mapper;


import com.bjf.pojo.BjfAddress;

import java.util.List;

public interface BjfAddressMapper {
    int deleteByPrimaryKey(Integer adId,Integer uId);

    int insert(BjfAddress record);

    int insertSelective(BjfAddress record);

    BjfAddress selectByPrimaryKey(Integer adId);

    List<BjfAddress>   selectByKey(Integer adId);

    int updateByPrimaryKeySelective(BjfAddress record);

    int updateByPrimaryKey(BjfAddress record);

    List<BjfAddress> selectAddressList(Integer uId);
    //    删除地址
    Integer deleteUserAddress(Integer adId);


}