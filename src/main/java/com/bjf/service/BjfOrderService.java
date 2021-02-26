package com.bjf.service;

import com.bjf.pojo.BjfOrder;
import com.bjf.pojo.BjfOrderItem;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

//订单表业务
public interface BjfOrderService {
    Map selectAll(Integer uid, Integer page, Integer size);
    int deleteOrder(String odDelid);
    Map selectOthers(Integer uid, Integer page, Integer size,int selectOthers);
    List selectOderItem(String odDelid,Integer status);//查询订单详情
    int updateBackMoney(String odDelid,String reasion,StringBuffer imag,Integer cmdId);//退货提交
    int updateOderTrue(String odDelid);//确认订单
    int updateNotBack(String odDelid);//取消订单
    List showBack(String odDelid);//退货展示
    BigDecimal showBackMoney(String odDelid, Integer oiId);//退货金额展示
    int notBackMoney(String odDelid,Integer oiId);//取消退款
    int upBackItem(String odDelid,Integer oiIds);//退款提交
    BjfOrderItem backItem(String odDelid, Integer oiId);//展示退款商品信息


    int insertSelective(BjfOrder record);
    BjfOrder selectByPrimaryKey(String odDelid);


    BjfOrder createOrder(Integer uId, Integer adId,
                       Boolean integralChecked,
                       Integer cnId, Boolean useMbTimes,
                       String message);

    BjfOrder getOrderByTradeNo(String outTradeNo);

    void changeOrderByNo(BjfOrder order);

    void delExOrderByNo(String outTradeNo);

}
