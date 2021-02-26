package com.bjf.controller;



import com.bjf.service.BjfCouponService;
import com.bjf.service.BjfMcouponService;
import com.bjf.service.BjfMdiscpService;
import org.springframework.amqp.rabbit.annotation.*;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RabbitMqListener {
   @Resource
   private BjfMdiscpService bjfMdiscpService;
   @Resource
   private BjfMcouponService mcouponService;
   @Resource
   private BjfCouponService bjfCouponService;

   //关闭发放
   @RabbitListener(bindings = {
           @QueueBinding(
                   value = @Queue(value = "close_delete",durable = "true"),
                   exchange = @Exchange(value = "mdiscp",type = "topic"),
                   key = {"mdiscp.close"}
           )
   })
   public void closeDelete(Integer id){
       if(id != null){
           mcouponService.deleteCoupon(id);
       }
   }



    //新人注册
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "new_user",durable = "true"),
                    exchange = @Exchange(value = "nuMans",type = "topic"),
                    key = {"nuMans.info"}
            )
    })
    public void newUserListen(Message message){
        Map map = (Map) message.getPayload();
        List<Integer> lists = (List<Integer>) map.get("list");//对象和时间
        Integer id = (Integer)map.get("id");
        System.out.println(map);
        System.out.println(lists);

        for (int i = 0;i < lists.size();i++) {
            System.out.println(lists.get(i));
            bjfMdiscpService.newUserGet(id,lists.get(i));
        }
        System.out.println("注册");
        System.out.println("lists:"+lists+"\t"+"id:"+id);
    }



    //用户登录
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "user_login",durable = "true"),
                    exchange = @Exchange(value = "login",type = "topic"),
                    key = {"login.info"}
            )
    })
    public void userLoginListen(Map map){
        List<Integer> lists = (List<Integer>) map.get("list");
        Integer id = (Integer)map.get("id");
        for (Integer list : lists) {
            bjfMdiscpService.userLoginGet(id,list);
        }
        System.out.println("登录");
    }

    //删除过期优惠券
    @RabbitListener(queues = "cp_queue")
    @RabbitHandler
    public void deleteCp(Integer cnId){
        bjfCouponService.deleteCoupon(cnId);
    }




}
