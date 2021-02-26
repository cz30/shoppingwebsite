package com.bjf.service.impl;



import com.bjf.mapper.BjfCommodityMapper;
import com.bjf.pojo.BjfCommodity;
import com.bjf.service.BjfCommodityService;
import com.bjf.service.ex.NotFoundException;
import com.bjf.util.PageRequest;
import com.bjf.util.PageResult;
import com.bjf.util.PageUtils;
import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/6
 * \* Time: 10:11
 * \* Description:
 * \
 */
@Service
@Transactional
public class BjfCommodityServiceImpl implements BjfCommodityService {
    @Resource
    BjfCommodityMapper bjfCommodityMapper;

    @Override
    public List<BjfCommodity> recommend() {
        return bjfCommodityMapper.recommend();
    }

    @Override
    public List<BjfCommodity> hot() {
        return bjfCommodityMapper.hot();
    }

    @Override
    public List<BjfCommodity> shouzhan() {
        return bjfCommodityMapper.shouzhan();
    }

    @Override
    public PageResult findPage(PageRequest pageRequest,Integer cgId) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest,cgId));
    }


    /**
     * 调用分页插件完成分页
     * @param
     * @return
     */
    private PageInfo<BjfCommodity> getPageInfo(PageRequest pageRequest, Integer cgId) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<BjfCommodity> sysMenus = bjfCommodityMapper.fenlei(cgId);
        return new PageInfo<BjfCommodity>(sysMenus);
    }

    @Override
    public BjfCommodity selectCommodityDetails(Integer cmdId) {
        return bjfCommodityMapper.selectCommodityDetails(cmdId);
    }

    @Override
    public List<BjfCommodity> selectVipCommodity() {
        return bjfCommodityMapper.selectVipCommodity();
    }

    @Override
    public BjfCommodity getOneByCmdId(Integer cmdId) {
        BjfCommodity commodity = bjfCommodityMapper.selectByPrimaryKey(cmdId);
        if (commodity == null) {
            throw new NotFoundException("商品不存在！");
        }
        return commodity;
    }
}
