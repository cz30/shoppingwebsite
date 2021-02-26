package com.bjf.service;

import com.bjf.pojo.BjfOrderItem;

import java.util.List;

public interface BjfOrderItemService {
    int updateSupportByDelidAndCmdId(Integer oiStatus,Integer oiId);

    int insertSelective(BjfOrderItem record);

    void addOneItem(BjfOrderItem item);

    List<BjfOrderItem> getOrderItems(String outTradeNo);

    void changeItem(BjfOrderItem item);
}
