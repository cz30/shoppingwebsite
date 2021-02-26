package com.bjf.controller;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
public class RabbitProviderController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisTemplate redisTemplate;





    //注册领取优惠券 登录领取优惠券
    @PostMapping("/cpget/new")
    public Integer newCpGet(Integer uid){
        System.out.println(redisTemplate.opsForHash().get("newcp","user:"+uid));
        try {
            if(redisTemplate.opsForHash().get("newcp","user:"+uid) != null){
                redisTemplate.opsForHash().delete("newcp","user:"+uid);
                return 1;//注册
            }else if(redisTemplate.opsForHash().get("ulohinget","user:"+uid) != null){
                redisTemplate.opsForHash().delete("ulohinget","user:"+uid);
                return 2;//登录
            }
        }catch (Exception e){
            return 0;
        }
        return 0;
    }



}
