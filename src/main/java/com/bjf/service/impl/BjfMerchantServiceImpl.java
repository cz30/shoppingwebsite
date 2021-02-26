package com.bjf.service.impl;

import com.bjf.mapper.BjfMerchantMapper;
import com.bjf.pojo.BjfMerchant;
import com.bjf.service.BjfMerchantService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class BjfMerchantServiceImpl implements BjfMerchantService {

    @Resource
    private BjfMerchantMapper bjfMerchantMapper;
    @Override
    public BjfMerchant queryBjfMerchantByName(String mcName) {
        return bjfMerchantMapper.queryBjfMerchantByName(mcName);
    }

    @Override
    public int insertBjfmer(BjfMerchant merchant) {
        return bjfMerchantMapper.insertBjfmer(merchant);
    }

    @Override
    public BjfMerchant queryBjfMerchantByPhoneNumber(String phoneNumber) {
        return bjfMerchantMapper.queryBjfMerchantByPhoneNumber(phoneNumber);
    }

    @Override
    public BjfMerchant queryBjfMerchantByEmail(String email) {
        return bjfMerchantMapper.queryBjfMerchantByEmail(email);
    }

    @Override
    public String selectLogo() {
        return bjfMerchantMapper.selectLogo();
    }

    @Override
    public BigDecimal getDpfeeByMcId(Integer mcId) {
        BigDecimal dpfee = bjfMerchantMapper.
                selectByPrimaryKey(mcId).getMcDpfee();
        return dpfee;
    }

    @Override
    public BjfMerchant getMerchant(Integer mcId) {
        return bjfMerchantMapper.selectByPrimaryKey(mcId);
    }

}
