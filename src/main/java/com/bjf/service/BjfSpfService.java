package com.bjf.service;


import com.bjf.pojo.BjfSpf;

public interface BjfSpfService {

    BjfSpf getBySpfId(Integer spfId);

    void reduceCount(Integer spfId, Integer num);

    Integer getCountById(Integer spfId);
}
