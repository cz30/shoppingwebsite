package com.bjf.mapper;



import com.bjf.pojo.BjfCommodity;

import java.util.List;

public interface BjfCommodityMapper {
    /**
     * 推荐商品展示
     *
     * @return
     */
    List<BjfCommodity> recommend();

    /**
     * 热销商品展示
     *
     * @return
     */
    List<BjfCommodity> hot();

    /**
     * 首页商品展示（15条数据）
     * 如果需要更改首页展示商品的数量需要去BjfCommodityDao.xml里把limit后面的值改掉
     */
    List<BjfCommodity> shouzhan();

    /**
     * 分页之后展示的商品
     * @param cgId
     * @return
     */
    List<BjfCommodity> fenlei(Integer cgId);

    /**
     * 商品详情页展示
      * @param cmdId
     * @return
     */
    BjfCommodity selectCommodityDetails(Integer cmdId);

    /**
     * 会员中心随机展示商品
     * @return
     */
    List<BjfCommodity> selectVipCommodity();

    BjfCommodity selectByPrimaryKey(Integer cmdId);

}