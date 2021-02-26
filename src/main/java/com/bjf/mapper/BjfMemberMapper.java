package com.bjf.mapper;

import com.bjf.pojo.BjfMember;
import com.bjf.pojo.vo.BjfAllMemberVo;

public interface BjfMemberMapper {
    int deleteByPrimaryKey(Integer mbId);

    int insert(BjfMember record);


    BjfMember selectByPrimaryKey(Integer mbId);


    int updateByPrimaryKey(BjfMember record);

    int selectCountByUserId(Integer u_id);


    /**
     * 根据用户id查询用户信息和会员信息
     * @param uId
     * @return
     */
    BjfAllMemberVo selectByuId(Integer uId);

    /**
     * 向会员表中插入数据
     * @param record
     * @return
     */
    int insertSelective(BjfMember record);

    /**
     * 根据用户id修改会员信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(BjfMember record);

    /**
     * 根据用户id查询会员信息
     * @param uId
     * @return
     */
    BjfMember selectByUIdKey(Integer uId);

    Integer selectMPByUId(Integer uId);

    void updMbTimesByUId(Integer uId);
}