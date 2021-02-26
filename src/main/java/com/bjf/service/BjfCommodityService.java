package com.bjf.service;


import com.bjf.pojo.BjfCommodity;
import com.bjf.util.PageRequest;
import com.bjf.util.PageResult;

import java.util.List;

public interface BjfCommodityService {
    List<BjfCommodity> recommend();
    List<BjfCommodity> hot();
    List<BjfCommodity> shouzhan();

    /**
     * 分页查询接口
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest, Integer cgId);

    BjfCommodity selectCommodityDetails(Integer cmdId);

    List<BjfCommodity> selectVipCommodity();

    BjfCommodity getOneByCmdId(Integer cmdId);
}
