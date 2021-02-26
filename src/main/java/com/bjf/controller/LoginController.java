package com.bjf.controller;

import com.bjf.pojo.BjfMerchant;
import com.bjf.pojo.BjfUser;
import com.bjf.service.BjfMerchantService;
import com.bjf.service.BjfUserService;
import com.bjf.util.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Controller
@CrossOrigin
public class LoginController {
    @Resource
    private BjfUserService userService;//用户业务
    @Resource
    private AfsCheckUtil afsCheckUtil;//阿里云滑块
    @Resource
    private GetCodeAndCheckUtil getCodeAndCheckUtil;//验证码验证
    @Resource
    private SendMessageUtil sendMessageUtil;//发送短信验证码
    @Resource
    private ShiroLoginUtil shiroLoginUtil;//用户登录
    @Resource
    private BjfMerchantService bjfMerchantService;//商家业务
    @Resource
    private SendMailUtil sendMailUtil;//发送邮箱
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;



    //======================用户登录=============================================
    @RequestMapping("/user/namelogin")
    @ResponseBody
    public Integer userNameLogin(String loginType, String sessionId, String username,String pwd, boolean rememberMe, HttpServletRequest request, HttpSession session,int choose){
        //System.out.println("loginType:"+loginType+"\n"+"sessionId:"+sessionId+"\n"+"username:"+username+"\n"+"pwd:"+pwd+"\n"+"rememberMe:"+rememberMe+"\n"+"choose:"+choose);
        boolean afs = afsCheckUtil.check(sessionId,request);
        System.out.println(afs);
        BjfUser bjfUser = new BjfUser();
       if (!afs){
            return null;
        }
            try{
                if(choose == 1) {//手机登录
                    bjfUser = userService.queryBjfUserByPhoneNumber(username);
                }else if(choose == 2){//邮箱登录
                    bjfUser = userService.queryBjfUserByEmail(username);
                }else if (choose ==  0){//账号登录
                    return shiroLoginUtil.bjfUserLogiin(loginType,username,pwd,rememberMe,session);
                }
            }catch (Exception e){
                return null;
            }
        rabbitTemplate.convertAndSend("user","user.login",bjfUser.getUId());
        return shiroLoginUtil.bjfUserLogiin(loginType,bjfUser.getUUsername(),bjfUser.getUPwd(),rememberMe,session);
    }

    //短信验证码登录
    @RequestMapping("/user/phoneLogin")
    @ResponseBody
    public Integer userPhoneLogin(String loginType,String phoneNumber,String code,Boolean rememberMe,HttpSession session){
        //System.out.println("LoginType:"+loginType + "\n" + "phoneNUmber:"+phoneNumber+"\n"+"rememberMe:"+rememberMe);
        if(getCodeAndCheckUtil.checkCode(phoneNumber,code) == 1){
            //验证通过
            BjfUser bjfUser = userService.queryBjfUserByPhoneNumber(phoneNumber);
            return shiroLoginUtil.bjfUserLogiin(loginType,bjfUser.getUUsername(),bjfUser.getUPwd(),rememberMe,session);
        }else {
            return null;
        }
    }

    //发送短信邮箱验证码
    @RequestMapping("/sendCode")
    @ResponseBody
    public int sendUserPhoneMessage(String number,String type){
        if(type.equals("phone")){
            if(sendMessageUtil.send(number)){
                return 1;//发送成功
            }else {
                return 0;//发送失败
            }
        }else if(type.equals("mail")){
            if(sendMailUtil.sendMail(number)){
                return 1;
            }else {
                return 0;
            }
        }
      return 0;
    }

    //======================商家登录========================
    //======================商家登录========================
    @RequestMapping("/merchant/nameLogin")
    @ResponseBody
    public Integer merchantLogin(String username,String pwd,String loginType,HttpServletRequest request,HttpSession session){
        BjfMerchant bjfMerchant = null;
        try{

            if (shiroLoginUtil.bjfMerchantLogin(loginType,username,pwd,session) == 1){
               // System.out.println("登录成功");
                return 1;
            }
           // System.out.println("登录失败");
            return 0;
        }catch (Exception e){
            return 0;
        }

    }

    //商家手机验证码登录
 /*   @RequestMapping("/merchant/phoneLogin")
    @ResponseBody
    public Integer merchantPhoneLogin(String loginType,String phoneNumber,String code,Model model,HttpSession session){
        if(getCodeAndCheckUtil.checkCode(phoneNumber,code) == 1){//验证通过
            BjfMerchant bjfMerchant = bjfMerchantService.queryBjfMerchantByPhoneNumber(phoneNumber);
            return shiroLoginUtil.bjfMerchantLogin(loginType,bjfMerchant.getMcUsername(),bjfMerchant.getMcPwd(),model,session);
        }else {
            model.addAttribute("merchantMsg","验证码错误");
            return 0;
        }
    }*/

}
