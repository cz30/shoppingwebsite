package com.bjf.mapper;


import com.bjf.pojo.BjfOrder;
import com.bjf.pojo.vo.BjfOrderVo;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

//订单表mapper
public interface BjfOrderMapper {
    BjfOrderVo selectAll(@Param("uid") Integer uid, @Param("odlid") String odlid);//查询所有订单
    Integer selectCount(@Param("uid") Integer uid);//查询所有订单条数
    List<BjfOrderVo> selectOthers(@Param("uid") Integer uid, @Param("page")Integer page, @Param("size") Integer size, @Param("odStatus") int odStatus);//获取对应订单状态的订单
    //查询为评价订单
    List<BjfOrderVo> selectNotSay(@Param("uid")Integer uid,@Param("page")Integer page,@Param("size")Integer size,@Param("odStatus") int status);
    int deleteOrder(String odDelid);//删除订单
    int selectCountByStatus(int uid,int status);//根据订单状态获取count
    int selectNotSayCountByStatus(@Param("uid") int uid,@Param("status") int status);//获取未评价订单的count
    List<BjfOrderVo> selectOderItem(String odDelid);//查询订单详情
    List<BjfOrderVo> selectNotPayOrderItem(String odDelid);//未支付订单详情
    int updateBackMoney(BjfOrder image);//申请退货
    int updateOderTrue(@Param("odDelid") String odDelid,@Param("status") Integer status);//确认订单
    int updateNotBack(String odDelid);//取消退货
    Integer selectOrderStatus(String odDelid);//查看订单状态
    BjfOrder selectGetOrder(String odDelid);//获取订单
    int updateOderStatus(@Param("odDelid") String odDelid,@Param("status") Integer status);//修改订单状态
    int selectUserId(String odDelid);//获取用户id
    List<String> selectOrderDelid(@Param("uid")Integer uid,@Param("page")Integer page,@Param("size")Integer size);//分页订单编号
    Integer updateOrderModTime(@Param("odDelid")String odDelid, @Param("odModifiedTime")Date odModifiedTime);//确认订单添加订单完成时间
    int count(Integer uid);

    List<BjfOrderVo> selectOther();

    int deleteByPrimaryKey(Integer odId);

    int insert(BjfOrder record);

    int insertSelective(BjfOrder record);

    BjfOrder selectByPrimaryKey(String odId);

    int updateByPrimaryKeySelective(BjfOrder record);

    int updateByPrimaryKey(BjfOrder record);

    BjfOrder selectByDelid(String delid);

    void deleteByTradeNo(String outTradeNo);
}