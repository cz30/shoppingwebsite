package com.bjf.service;

import com.bjf.pojo.BjfMember;
import com.bjf.pojo.vo.BjfAllMemberVo;

public interface BjfMemberService {
    int deleteByPrimaryKey(Integer mbId);

    int insert(BjfMember record);

    BjfMember selectByPrimaryKey(Integer mbId);

    int updateByPrimaryKey(BjfMember record);

    int selectCountByUserId(Integer u_id);





    BjfAllMemberVo selectByuId(Integer uId);

    int insertSelective(BjfMember record);

    int updateByPrimaryKeySelective(BjfMember record);

    BjfMember selectByUIdKey(Integer uId);


    Integer getMPByUId(Integer uId);

    void reduceMbTimes(Integer uId);
}
