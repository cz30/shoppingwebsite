package com.bjf.mapper;

import com.bjf.pojo.BjfOrderItem;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BjfOrderItemMapper {
    List<BjfOrderItem> selectAll(String odDelid);//获取所有商品信息
    List<BjfOrderItem> selectAllBack(String odDelid);//获取退款商品信息
    BjfOrderItem selectBackItem(@Param("odDelid")String odDelid,@Param("odId")Integer oiId);//获取单个商品的信息
    List<BjfOrderItem> selectAllSupport(String odDelid);//获取所有支持退款的商品
    int updateItemSupport(@Param("odDelid") String odDelid, @Param("oiId") Integer oiId);//
    String selectGoodsBackTime(@Param("odDelid")String odDelid,@Param("cmdId")Integer cmdId);//查询商品退货到期时间
    int updateOIStatus(@Param("odDelid")String odDelid,@Param("oiId")Integer oiId,@Param("oiStatus")Integer oiStatus);//修改订单详情中商品退款状态

    /**
     * 用户评价完更改订单详情表的状态
     * @param oiStatus
     * @param cmdId
     * @param odDelid
     * @return
     */
    int updateSupportByDelidAndCmdId(Integer oiStatus,Integer oiId);

    int deleteByPrimaryKey(Integer oiId);

    int insert(BjfOrderItem record);

    int insertSelective(BjfOrderItem record);

    BjfOrderItem selectByPrimaryKey(Integer oiId);

    int updateByPrimaryKeySelective(BjfOrderItem record);

    int updateByPrimaryKey(BjfOrderItem record);

    String selectByDelid(String odDelid);

    List<BjfOrderItem> findItemsByNo(String outTradeNo);
}