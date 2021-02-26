package com.bjf.util;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RabbitMQUtil {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendTimeAndDelete(Integer cnid,String time){
        rabbitTemplate.convertAndSend("cp_dlx_exchange", "cp_ttl_routes", cnid, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(time);
                return message;
            }
        });
    }

}
