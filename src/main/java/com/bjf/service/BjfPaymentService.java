package com.bjf.service;


import com.bjf.pojo.BjfPayment;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/13
 * \* Time: 16:21
 * \* Description:
 * \
 */
public interface BjfPaymentService {
    void savePayment(BjfPayment bjfPayment);

    BjfPayment selectBypayOutTradeNo(String payOutTradeNo);

    int updateByPayOutTradeNoSelective(BjfPayment record);

    void insertOnePayment(BjfPayment payment);

    BjfPayment getPayment(String out_trade_no);

    void updatePayment(BjfPayment payment);
}
