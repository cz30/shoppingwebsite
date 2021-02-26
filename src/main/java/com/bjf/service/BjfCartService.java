package com.bjf.service;

import com.bjf.pojo.BjfCart;

import java.util.List;

public interface BjfCartService {

    /**
     * 添加商品至购物车
     * @param uId
     * @param spfId
     * @param amount
     */
    Boolean addToCart(Integer uId, Integer spfId, Integer amount);

    /**
     * 根据用户id得到当前用户的所有购物车数据
     * @param uId
     * @return
     */
    List<BjfCart> getCartList(Integer uId);

    /**
     * 删除购物车数据
     * @param cartIds
     */
    void moveToCart(Integer uId, Integer[] cartIds);

    /**
     * 保存购物车数据至结算页
     * @param uId
     * @param reloadCartAmount JSON字符串转成CartVO对象的list集合:List<CartVO>{{cartId, amount}. {cartId, amount}}
     */
    List<BjfCart> saveCartList(Integer uId, String reloadCartAmount);

    List<BjfCart> getCartCheckedList(Integer uId);

    Boolean saveCartNow(Integer uId, Integer spfId, Integer amount);
}
