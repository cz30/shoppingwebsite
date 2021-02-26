package com.bjf.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {



    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("cp_exchange",true,false);
    }

    @Bean
    public DirectExchange dlxExchange(){
        return new DirectExchange("cp_dlx_exchange",true,false);
    }

    //死信队
    @Bean
    public Queue regDlxQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","cp_exchange");
        arguments.put("x-dead-letter-routing-key","cp_delete");
        return new Queue("cp_queuedlx",true,false,false,arguments);
    }

    @Bean
    public Queue regQueue(){
        return new Queue("cp_queue",true);
    }
    //绑定
    @Bean
    public Binding regQueueExchange(){
        return new Binding("cp_queue",Binding.DestinationType.QUEUE,"cp_exchange","cp_delete",null);
    }

    //死信绑定
    @Bean
    public Binding regDlxQueueExchange(){
        return new Binding("cp_queuedlx",Binding.DestinationType.QUEUE,"cp_dlx_exchange","cp_ttl_routes",null);
    }

 /*   //登录队列
    @Bean
    public Queue loginQueue(){
        return new Queue("login_queue",true);
    }

    //登录死信队列
    @Bean
    public Queue loginDlxQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","cp_exchange");
        arguments.put("x-dead-letter-routing-key","login_reg");
        return new Queue("login_queuedlx",true,false,false,arguments);
    }

    //登录绑定
    public Binding loginQueueExchange(){
        return new Binding("login_queue",Binding.DestinationType.QUEUE,"cp_exchange","login_reg",null);
    }

    //登录死信绑定
    @Bean
    public Binding loginDlxQueueExchange(){
        return new Binding("login_queuedlx",Binding.DestinationType.QUEUE,"cp_dlx_exchange","login_ttl_routes",null);
    }*/




}
