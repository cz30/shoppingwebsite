package com.bjf.mapper;

import com.bjf.pojo.BjfSpf;

public interface BjfSpfMapper {
    int deleteByPrimaryKey(Integer spfId);

    int insert(BjfSpf record);

    int insertSelective(BjfSpf record);

    BjfSpf selectByPrimaryKey(Integer spfId);

    int updateByPrimaryKeySelective(BjfSpf record);

    int updateByPrimaryKey(BjfSpf record);
}