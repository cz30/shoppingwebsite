package com.bjf.service;



import com.bjf.pojo.BjfMemberPay;

import java.util.List;

public interface BjfMemberPayService {
    List<BjfMemberPay> selectAll();

    BjfMemberPay selectByPrimaryKey(Integer mbpId);
}
