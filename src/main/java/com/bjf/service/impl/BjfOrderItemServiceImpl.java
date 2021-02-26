package com.bjf.service.impl;

import com.bjf.mapper.BjfOrderItemMapper;
import com.bjf.pojo.BjfOrderItem;
import com.bjf.service.BjfOrderItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BjfOrderItemServiceImpl implements BjfOrderItemService {
    @Resource
    BjfOrderItemMapper bjfOrderItemMapper;

    @Override
    public int updateSupportByDelidAndCmdId(Integer oiStatus,Integer odId) {
        return bjfOrderItemMapper.updateSupportByDelidAndCmdId(oiStatus,odId);
    }

    @Override
    public int insertSelective(BjfOrderItem record) {
        return bjfOrderItemMapper.insertSelective(record);
    }

    @Override
    public void addOneItem(BjfOrderItem item) {
        bjfOrderItemMapper.insertSelective(item);
    }

    @Override
    public List<BjfOrderItem> getOrderItems(String outTradeNo) {
        return bjfOrderItemMapper.findItemsByNo(outTradeNo);
    }

    @Override
    public void changeItem(BjfOrderItem item) {
        bjfOrderItemMapper.updateByPrimaryKeySelective(item);
    }
}
