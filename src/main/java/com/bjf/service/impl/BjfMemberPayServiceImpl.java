package com.bjf.service.impl;


import com.bjf.mapper.BjfMemberPayMapper;
import com.bjf.pojo.BjfMemberPay;
import com.bjf.service.BjfMemberPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/13
 * \* Time: 15:24
 * \* Description:
 * \
 */
@Service
public class BjfMemberPayServiceImpl implements BjfMemberPayService {

    @Resource
    BjfMemberPayMapper bjfMemberPayMapper;
    @Override
    public List<BjfMemberPay> selectAll() {
        return bjfMemberPayMapper.selectAll();
    }

    @Override
    public BjfMemberPay selectByPrimaryKey(Integer mbpId) {
        return bjfMemberPayMapper.selectByPrimaryKey(mbpId);
    }
}
