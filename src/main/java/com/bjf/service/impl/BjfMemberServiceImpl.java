package com.bjf.service.impl;

import com.bjf.mapper.BjfMemberMapper;
import com.bjf.pojo.BjfMember;
import com.bjf.pojo.vo.BjfAllMemberVo;
import com.bjf.service.BjfMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BjfMemberServiceImpl implements BjfMemberService {

    @Resource
    private BjfMemberMapper bjfMemberMapper;

    @Override
    public int deleteByPrimaryKey(Integer mbId) {
        return 0;
    }

    @Override
    public int insert(BjfMember record) {
        return 0;
    }


    @Override
    public BjfMember selectByPrimaryKey(Integer mbId) {
        return null;
    }


    @Override
    public int updateByPrimaryKey(BjfMember record) {
        return 0;
    }

    @Override
    public int selectCountByUserId(Integer u_id) {
        return bjfMemberMapper.selectCountByUserId(u_id);
    }

    @Override
    public BjfAllMemberVo selectByuId(Integer uId) {
        return bjfMemberMapper.selectByuId(uId);
    }

    @Override
    public int insertSelective(BjfMember record) {
        return bjfMemberMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(BjfMember record) {
        return bjfMemberMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public BjfMember selectByUIdKey(Integer uId) {
        return bjfMemberMapper.selectByUIdKey(uId);
    }

    @Override
    public Integer getMPByUId(Integer uId) {
        return bjfMemberMapper.selectMPByUId(uId);
    }

    @Override
    public void reduceMbTimes(Integer uId) {
        bjfMemberMapper.updMbTimesByUId(uId);
    }

}
