package com.bjf.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 短信验证码随机四位数
 */
@Component
public class GetCodeAndCheckUtil {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    //生成随机验证码
    public String code(){
        String base = "0123456789";
        int size = base.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        //设置验证码长度
        for(int i=1;i<=4;i++){
            //产生0到size-1的随机值
            int index = r.nextInt(size);
            //在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            //将c放入到StringBuffer中去
            sb.append(c);
        }
        return sb.toString();
    }

    //验证码验证
    public Integer checkCode(String number,String code){
        if(code.equals(redisTemplate.opsForValue().get(number))){
            return 1;
        }else {
            return 0;
        }
    }
}
