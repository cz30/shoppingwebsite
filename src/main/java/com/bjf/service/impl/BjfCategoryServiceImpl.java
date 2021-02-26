package com.bjf.service.impl;


import com.bjf.mapper.BjfCategoryMapper;
import com.bjf.pojo.BjfCategory;
import com.bjf.service.BjfCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/6
 * \* Time: 8:54
 * \* Description:
 * \
 */
@Service
@Transactional
public class BjfCategoryServiceImpl implements BjfCategoryService {
    
    @Resource
    BjfCategoryMapper bjfCategoryMapper;
    
    
    @Override
    public List<BjfCategory> getBjfCategoryTree() {
        return bjfCategoryMapper.getBjfCategoryTree();
    }
}
