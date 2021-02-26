package com.bjf.service.impl;


import com.bjf.mapper.BjfPaymentMapper;
import com.bjf.pojo.BjfPayment;
import com.bjf.service.BjfPaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/13
 * \* Time: 16:21
 * \* Description:
 * \
 */
@Service
public class BjfPaymentServiceImpl implements BjfPaymentService {

    @Resource
    BjfPaymentMapper bjfPaymentMapper;


    @Override
    public void savePayment(BjfPayment bjfPayment) {
        bjfPaymentMapper.insertSelective(bjfPayment);
    }

    @Override
    public BjfPayment selectBypayOutTradeNo(String payOutTradeNo) {
        return bjfPaymentMapper.selectBypayOutTradeNo(payOutTradeNo);
    }

    @Override
    public int updateByPayOutTradeNoSelective(BjfPayment record) {
        int i=bjfPaymentMapper.updateByPayOutTradeNoSelective(record);

        return i;
    }

    @Override
    public void insertOnePayment(BjfPayment payment) {
        bjfPaymentMapper.insertSelective(payment);
    }

    @Override
    public BjfPayment getPayment(String outTradeNo) {
        return bjfPaymentMapper.selectByOutTradeNo(outTradeNo);
    }

    @Override
    public void updatePayment(BjfPayment payment) {
        bjfPaymentMapper.updateByPrimaryKeySelective(payment);
    }


}
