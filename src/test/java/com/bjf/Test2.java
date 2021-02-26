package com.bjf;

import com.alibaba.fastjson.JSON;
import com.bjf.pojo.BjfCart;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class Test2 {
    @Resource(name = "stringRedisTemplate")
    HashOperations<String,String,String> opsForHash;
    @Test
    void test1(){
        String cartKey = "user:" + 120 +":cart";
        String cartCheckedKey = "user:" + 120 + ":cartChecked";
        List<String> stringList = opsForHash.values(cartCheckedKey);
        if (stringList != null) {
            for (String s : stringList) {
                BjfCart cart = JSON.parseObject(s, BjfCart.class);
                opsForHash.delete(cartCheckedKey, String.valueOf(cart.getCartId()));
            }
        }
    }


}
