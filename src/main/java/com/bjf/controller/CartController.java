package com.bjf.controller;

import com.bjf.pojo.BjfCart;
import com.bjf.service.BjfCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * \* Description:
 * \* User: mortal
 * \* Date: 2020/9/24
 * \* Time: 13:40
 * \
 */
@CrossOrigin
@Controller
@RequestMapping("carts")
public class CartController {

    @Autowired
    private BjfCartService cartService;

    /**
     * 添加商品至购物车
     * @param spfId
     * @param amount
     */
    @RequestMapping("add/to/cart")
    @ResponseBody
    public Boolean addToCart(/*HttpSession session*/Integer uId, Integer spfId,
                          Integer amount) {
        //Integer uId = getUId(session);
        System.err.println("\tuId=" + uId);
        Boolean flag = cartService.addToCart(uId, spfId, amount);
        return flag;
    }

    /**
     * 获取该用户的购物车数据
     * @return
     */
    @RequestMapping("get/list")
    @ResponseBody
    public List<BjfCart> getCartList(/*HttpSession session*/Integer uId) {
        //Integer uId = getUId(session);
        List<BjfCart> data = cartService.getCartList(uId);
        System.out.println(data);
        return data;
    }

    /**
     * 删除购物车数据
     * @param cartId
     */
    @RequestMapping("move/to/cart")
    @ResponseBody
    public void moveToCart(/*HttpSession session*/Integer uId, Integer[] cartId) {
        //Integer uId = getUId(session);
        cartService.moveToCart(uId, cartId);
    }

    @RequestMapping("reload/cart")
    @ResponseBody
    public void reloadCartAmount(/*HttpSession session*/Integer uId, String reloadCartAmount) {
        //Integer uId = (Integer) session.getAttribute("uId");

        List<BjfCart> itemList = cartService.saveCartList(uId, reloadCartAmount);
        System.err.println(itemList);
    }

    @RequestMapping("buy/now")
    @ResponseBody
    public boolean buyNow(/*HttpSession session*/Integer uId, Integer spfId, Integer amount) {
        //Integer uId = (Integer) session.getAttribute("uId");

        Boolean flag = cartService.saveCartNow(uId, spfId, amount);
        return flag;
    }

}
