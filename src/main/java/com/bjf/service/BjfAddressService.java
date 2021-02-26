package com.bjf.service;


import com.bjf.pojo.BjfAddress;

import java.util.List;

public interface BjfAddressService {
    public List<BjfAddress> selectByKey(Integer userId);
    public Integer deletByAdId(Integer adId,Integer uId);

    int insertOrUpDataAddress(BjfAddress bjfAddress);
    String  addressCoordinates(String address);

    BjfAddress getOneAddress(Integer aid);

    List<BjfAddress> getAddressList(Integer uId);

    Integer addNewAddress(BjfAddress address);

    void changeAddress(BjfAddress address);

//    删除地址
    Integer deleteAddress(Integer adId);
//    删除地址的逻辑判断
    Integer deleteAddressByUserId(Integer uId,Integer adId, boolean type);
}
