package com.bjf.service.impl;


import com.bjf.mapper.BjfMenberRightsMapper;
import com.bjf.pojo.BjfMenberRights;
import com.bjf.service.BjfMenberRightsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/13
 * \* Time: 14:28
 * \* Description:
 * \
 */
@Service
public class BjfMenberRightsServiceImpl implements BjfMenberRightsService {

    @Resource
    BjfMenberRightsMapper bjfMenberRightsMapper;

    @Override
    public List<BjfMenberRights> selectAll() {
        return bjfMenberRightsMapper.selectAll();
    }
}
