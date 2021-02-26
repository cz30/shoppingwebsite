package com.bjf.mapper;


import com.bjf.pojo.BjfMemberPay;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface BjfMemberPayMapper {
    List<BjfMemberPay> selectAll();
    BjfMemberPay selectByPrimaryKey(Integer mbpId);

}