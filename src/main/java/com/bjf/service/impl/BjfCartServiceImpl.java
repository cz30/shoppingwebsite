package com.bjf.service.impl;

import com.alibaba.fastjson.JSON;
import com.bjf.mapper.BjfCartMapper;
import com.bjf.pojo.BjfCart;
import com.bjf.pojo.BjfCommodity;
import com.bjf.pojo.BjfSpf;
import com.bjf.pojo.vo.CartVO;
import com.bjf.service.BjfCartService;
import com.bjf.service.BjfCommodityService;
import com.bjf.service.BjfSpfService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * \* Description:
 * \* User: mortal
 * \* Date: 2020/9/24
 * \* Time: 13:40
 * \
 */
@Service
public class BjfCartServiceImpl implements BjfCartService {

    @Resource(name= "stringRedisTemplate")
    private HashOperations<String,String,String> opsForHash;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private BjfCartMapper cartMapper;

    @Autowired
    private BjfSpfService spfService;

    @Autowired
    private BjfCommodityService commodityService;

    @Override
    public Boolean addToCart(Integer uId, Integer spfId, Integer amount) {
        // 创建当前时间
        Date now = new Date();
        // 定义redis的购物车key
        String cartKey = "user:" + uId +":cart";
        // 先找出该规格商品
        BjfSpf spf = spfService.getBySpfId(spfId);
        BjfCommodity commodity = commodityService.getOneByCmdId(spf.getCmdId());
        BjfCart result = cartMapper.findOneCart(uId, spfId);
        // 判断有无此购物车数据！
        Integer count = spfService.getCountById(spfId);
        if (result != null) {
            if ((result.getCartNum() + amount) > count) {
                return false;
            }
            result.setCartNum(result.getCartNum() + amount);
            result.setCartOprice(spf.getSpfPrice());
            result.sumPrice();
            cartMapper.updateByPrimaryKeySelective(result);
        } else {
            if (amount > count) {
                return false;
            }
            BjfCart cart = new BjfCart();
            cart.setCartImage(spf.getSpfImage());
            cart.setCartOprice(spf.getSpfPrice());
            cart.setCartTitle(commodity.getCmdName());
            cart.setCartNum(amount);
            cart.sumPrice();
            cart.setUId(uId);
            cart.setCmdId(spf.getCmdId());
            cart.setSpfId(spfId);
            cart.setCartTime(now);
            cart.setCartContent(spf.getSpfContent());
            cart.setSpfCount(spf.getSpfCount());
            BigDecimal cmdDiscount = commodity.getCmdDiscount();
            if (cmdDiscount == null || cmdDiscount.compareTo(BigDecimal.valueOf(0)) == 0) {
                cmdDiscount = BigDecimal.valueOf(1);
            }
            cart.setDiscount(cmdDiscount);

            // 插入到数据库
            cartMapper.insertSelective(cart);
            result = cart;
        }
        opsForHash.put(cartKey, String.valueOf(result.getCartId()), JSON.toJSONString(result));
        return true;
    }

    @Override
    public List<BjfCart> getCartList(Integer uId) {
        // 声明一个空集合用来存储购物车数据
        List<BjfCart> cartList = new ArrayList<>();

        // 定义购物车的key
        String cartKey = "user:" + uId +":cart";

        // 根据key获取缓存里的String（购物车数据）集合！
        List<String> stringList = opsForHash.values(cartKey);
        // 如果缓存里存在，进行遍历添加到cartList
        if (stringList != null && stringList.size() > 0) {
            for (String cartStr : stringList) {
                cartList.add(JSON.parseObject(cartStr, BjfCart.class));
            }

            // 重写排序！根据添加时间降序排序
            cartList.sort(new Comparator<BjfCart>() {
                @Override
                public int compare(BjfCart o1, BjfCart o2) {
                    return o2.getCartTime().compareTo(o1.getCartTime());
                }
            });
        } else {
            // 否则不存在，从数据库里获取！
            cartList = cartMapper.findByUId(uId);
        }
        System.err.println(cartList);
        // 遍历新集合，重新计算小计！
        for (BjfCart cart : cartList) {
            cart.sumPrice();
            cart.setSpfCount(spfService.getCountById(cart.getSpfId()));
        }
        return cartList;
    }

    @Override
    public void moveToCart(Integer uId, Integer[] cartIds) {
        String cartKey = "user:" + uId +":cart";
        String cartCheckedKey = "user:" + uId + ":cartChecked";
        for (Integer cartId : cartIds) {
            opsForHash.delete(cartKey, String.valueOf(cartId));
            opsForHash.delete(cartCheckedKey, String.valueOf(cartId));
        }
        for (Integer cartId : cartIds) {
            cartMapper.deleteByPrimaryKey(cartId);
        }
    }

    @Override
    public List<BjfCart> saveCartList(Integer uId, String reloadCartAmount) {
        // 保存准备结算的购物车数据开始...
        String cartKey = "user:" + uId +":cart";
        String cartCheckedKey = "user:" + uId + ":cartChecked";

        redisTemplate.delete(cartCheckedKey);

        /*List<String> stringList = opsForHash.values(cartCheckedKey);
        System.out.println("开始判断缓存中是否有结算数据");
        System.out.println(opsForHash.values(cartCheckedKey));
        if (stringList != null) {
            for (String s : stringList) {
                System.out.println("开始遍历结算数据中的值");
                BjfCart cart = JSON.parseObject(s, BjfCart.class);
                System.out.println(opsForHash.get(cartCheckedKey,String.valueOf(cart.getCartId())));
                opsForHash.delete(cartCheckedKey, String.valueOf(cart.getCartId()));
                System.out.println(opsForHash.get(cartCheckedKey,String.valueOf(cart.getCartId())));
            }
        }
        System.out.println(opsForHash.values(cartCheckedKey));
        System.out.println("结算循环结束");*/
        List<CartVO> cartVOList = JSON.parseArray(reloadCartAmount, CartVO.class);
        List<BjfCart> carts = new ArrayList<>();
        System.out.println("开始遍历VO中的数据");
        for (CartVO vo : cartVOList) {
            Integer cartId = vo.getCartId();
            Integer amount = vo.getAmount();
            // 得到一条购物车的Json数据
            String cartJson = opsForHash.get(cartKey, String.valueOf(cartId));
            // 把其转成cart类型的对象
            BjfCart cart = JSON.parseObject(cartJson, BjfCart.class);
            cart.setCartNum(amount);
            cart.sumPrice();
            System.out.println("开始往缓存中存入结算数据");
            System.out.println(cart);
            opsForHash.put(cartKey, String.valueOf(cartId), JSON.toJSONString(cart));
            opsForHash.put(cartCheckedKey, String.valueOf(cartId), JSON.toJSONString(cart));
            cartMapper.updateByPrimaryKeySelective(cart);
            carts.add(cart);
        }
        System.out.println(carts);
        System.out.println("结算存入缓存结束");
        return carts;
    }

    @Override
    public List<BjfCart> getCartCheckedList(Integer uId) {
        String cartCheckedKey = "user:" + uId + ":cartChecked";
        // 声明一个空集合用来存储购物车数据
        List<BjfCart> carts = new ArrayList<>();
        List<String> stringList = opsForHash.values(cartCheckedKey);
        for (String cartStr : stringList) {
            carts.add(JSON.parseObject(cartStr, BjfCart.class));
        }
        return carts;
    }

    @Override
    public Boolean saveCartNow(Integer uId, Integer spfId, Integer amount) {
        String cartCheckedKey = "user:" + uId + ":cartChecked";
        BjfSpf spf = spfService.getBySpfId(spfId);
        BjfCommodity cmd = commodityService.getOneByCmdId(spf.getCmdId());
        if (spf.getSpfCount() < amount) {
            return false;
        }
        BjfCart cart = new BjfCart();
        cart.setCartImage(spf.getSpfImage());
        cart.setCartOprice(spf.getSpfPrice());
        cart.setCartNum(amount);
        cart.sumPrice();
        cart.setUId(uId);
        cart.setCmdId(spf.getCmdId());
        cart.setCartTitle(cmd.getCmdName());
        cart.setSpfId(spfId);
        cart.setCartTime(new Date());
        cart.setCartContent(spf.getSpfContent());
        cart.setSpfCount(spf.getSpfCount());
        cart.setDiscount(cmd.getCmdDiscount());
        opsForHash.put(cartCheckedKey, "0", JSON.toJSONString(cart));
        // 同时加入缓存！
//        System.out.println("JSON 初始化资源----------->"+ JSON.toJSONString(result));
        return true;
    }

}
