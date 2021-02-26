package com.bjf.controller;

import com.bjf.service.BjfUserService;
import com.bjf.util.GetCodeAndCheckUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Controller
@CrossOrigin
public class UpdatePasswordController {
    @Resource
    private GetCodeAndCheckUtil getCodeAndCheckUtil;//验证验证码
    @Resource
    private BjfUserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/update/test")
    @ResponseBody
    public void test(){
        redisTemplate.opsForValue().set("1287263707@qq.com","3304",30, TimeUnit.MINUTES);
        System.out.println(111);
    }

    @RequestMapping("/update/byCode")
    @ResponseBody
    public Integer updateByEmail(String code,String number,String password,String pwd,String type){
        System.out.println("code:" + code + "\n" + "number:"+number + "\n"+"password:"+password +"\n" +"pwd:"+pwd+"\n"+"type:"+type+"\n");
        try{
            if(type.equals("mail")){
                Integer uid = userService.queryBjfUserByEmail(number).getUId();
                if (uid != null){
                    if(userService.queryUserEmail(number) == 1){
                        if (getCodeAndCheckUtil.checkCode(number,code).equals(1)){
                            return userService.updatePassword(password,pwd,uid);
                        }else {
                            return 0;
                        }
                    }
                }
            }else if(type.equals("phone")){
                Integer uid = userService.queryUserPhoneNumber(number);
                System.out.println(userService.queryUserPhoneNumber(number));
                if(uid != null){
                    if (userService.queryUserPhoneNumber(number) == 1){
                        if(getCodeAndCheckUtil.checkCode(number,code).equals(1)){
                            return userService.updatePassword(password,pwd,uid);
                        }else {
                            return 0;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

}
