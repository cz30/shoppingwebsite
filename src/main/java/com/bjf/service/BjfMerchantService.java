package com.bjf.service;


import com.bjf.pojo.BjfMerchant;

import java.math.BigDecimal;

public interface BjfMerchantService {
    BjfMerchant queryBjfMerchantByName(String mcName);
    int insertBjfmer(BjfMerchant merchant);
    BjfMerchant queryBjfMerchantByPhoneNumber(String phoneNumber);
    BjfMerchant queryBjfMerchantByEmail(String email);

    String selectLogo();

    BigDecimal getDpfeeByMcId(Integer mcId);

    BjfMerchant getMerchant(Integer mcId);
}
