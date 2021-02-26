package com.bjf.mapper;


import com.bjf.pojo.BjfMenberRights;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

@CacheNamespace
public interface BjfMenberRightsMapper {


    List<BjfMenberRights> selectAll();


}