package com.bjf.mapper;


import com.bjf.pojo.BjfBackment;
import org.springframework.data.repository.query.Param;

public interface BjfBackmentMapper {
    int insertBackOrderItems(BjfBackment bjfBackment); //存入退款商品信息及退款金额
    int delectBackOrderItems(@Param("odDelid")String odDelid,@Param("oiId") Integer oiId);//删除取消退款的商品
}