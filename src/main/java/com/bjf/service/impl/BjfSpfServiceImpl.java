package com.bjf.service.impl;

import com.bjf.mapper.BjfSpfMapper;
import com.bjf.pojo.BjfSpf;
import com.bjf.service.BjfSpfService;
import com.bjf.service.ex.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BjfSpfServiceImpl implements BjfSpfService {
    @Autowired
    private BjfSpfMapper spfMapper;

    @Override
    public BjfSpf getBySpfId(Integer spfId) {
        BjfSpf spf = spfMapper.selectByPrimaryKey(spfId);
        if (spf == null) {
            throw new NotFoundException("没有这个规格啊！");
        }
        return spf;
    }

    @Override
    public synchronized void reduceCount(Integer spfId, Integer num) {
        BjfSpf spf = spfMapper.selectByPrimaryKey(spfId);
        if (spf == null) {
            throw new NotFoundException("没有这个规格啊！");
        }
        int i = spf.getSpfCount() - num;
        spf.setSpfCount(i);
        spfMapper.updateByPrimaryKeySelective(spf);
    }

    @Override
    public Integer getCountById(Integer spfId) {
        return spfMapper.selectByPrimaryKey(spfId).getSpfCount();
    }

}
