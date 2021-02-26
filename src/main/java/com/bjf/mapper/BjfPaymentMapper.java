package com.bjf.mapper;




import com.bjf.pojo.BjfPayment;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface BjfPaymentMapper {
    int updateSupportMoney(@Param("money") BigDecimal money, @Param("odDelid") String odDelid);//添加退款金额
    int deleteBackMoney(String odDelid);//删除退款数据
    String selectAliPayTrandNo(String odDelid);//查询阿里订单交易编码

    int deleteByPrimaryKey(Integer payId);

    int insert(BjfPayment record);

    int insertSelective(BjfPayment bjfPayment);

    BjfPayment selectByPrimaryKey(Integer payId);
    BjfPayment selectBypayOutTradeNo(String payOutTradeNo);

    int updateByPayOutTradeNoSelective(BjfPayment record);

    int updateByPrimaryKey(BjfPayment record);

    BjfPayment selectByOutTradeNo(String outTradeNo);

    void updateByPrimaryKeySelective(BjfPayment payment);
}