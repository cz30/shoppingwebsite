package com.bjf.mapper;



import com.bjf.pojo.BjfCategory;

import java.util.List;

public interface BjfCategoryMapper {
    /**
     * 获取类目
     * @return
     */
    List<BjfCategory> getBjfCategoryTree();
}