package com.bjf.mapper;

import com.bjf.pojo.BjfMerchant;

public interface BjfMerchantMapper {
    BjfMerchant queryBjfMerchantByName(String mcName);
    int insertBjfmer(BjfMerchant merchant);
    BjfMerchant queryBjfMerchantByPhoneNumber(String phoneNumber);
    BjfMerchant queryBjfMerchantByEmail(String email);

    /**
     * 获取商家logo展示在首页
     * @return
     */
    String selectLogo();

    BjfMerchant selectByPrimaryKey(Integer mcId);
}