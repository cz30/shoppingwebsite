package com.bjf.controller;

import com.bjf.pojo.BjfMerchant;
import com.bjf.pojo.BjfUser;
import com.bjf.service.BjfMerchantService;
import com.bjf.service.BjfUserService;
import com.bjf.util.GetCodeAndCheckUtil;
import com.bjf.util.ImgFileUploadUtill;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 商家用户注册
 */
@Controller
@CrossOrigin
public class RegisterController {
    @Resource
    private BjfUserService bjfUserService;
    @Resource
    private BjfMerchantService bjfMerchantService;
    @Resource
    private ImgFileUploadUtill imgFileUploadUtill;//文件上传
    @Resource
    private GetCodeAndCheckUtil getCodeAndCheckUtil;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;


   /* @RequestMapping("/test")
    @ResponseBody
    public Object test(Integer i){
        return redisTemplate.opsForValue().get("user:"+i);
    }*/

    //===================用户注册======================
    //检查名字
    @RequestMapping("/register/checkuname")
    @ResponseBody
    public Integer checkUName(String username){
        return bjfUserService.queryBjfUserName(username);
       /* if(bjfUserService.queryBjfUserByName(username) == null){
            return 0;
        }else {
            return 1;
        }*/
    }

    //检查手机号
    @RequestMapping("/register/checkuphone")
    @ResponseBody
    public Integer checkUPhone(String phoneNumber){
        System.out.println(bjfUserService.queryUserPhoneNumber(phoneNumber) == 1);
        if (bjfUserService.queryUserPhoneNumber(phoneNumber) == 1){
            return 0;
        }else {
            return 1;
        }
    }

    //检查邮箱
    @RequestMapping("/register/checkuemail")
    @ResponseBody
    public Integer checkUEmail(String email){
        if(bjfUserService.queryUserEmail(email) == 1){
            return 0;
        }else{
            return 1;
        }
    }

    //注册
    @RequestMapping("/register/user")
    @ResponseBody
    public Integer userRegister(BjfUser bjfUser, Model model){
        System.out.println(bjfUser);
        int insert;
        bjfUser.setUMember(0);
        String account = getCodeAndCheckUtil.code() + bjfUser.getUPhoneNumber().substring(0,4);
        bjfUser.setUAccount(account);
        if(bjfUserService.queryUserEmail(bjfUser.getUEmail())!=1 || bjfUser.getUEmail() == null){
            System.out.println("email");
            //if(bjfUserService.queryBjfUserName(bjfUser.getUUsername()) != 1 && bjfUserService.queryUserPhoneNumber(bjfUser.getUPhoneNumber()) != 1){
                System.out.println("user and phone");
                insert = bjfUserService.insertBjfUser(bjfUser);
                BjfUser user = bjfUserService.queryBjfUserByName(bjfUser.getUUsername());
                //redisTemplate.opsForValue().set("user:"+user.getUId(),user);
                rabbitTemplate.convertAndSend("user","user.register",user.getUId());
                return insert;
           /* }else {
                return 0;
            }*/
        }
        return 0;


     /*   if(insert == 1){
            model.addAttribute("uregister","注册成功");
            return 1;
        }else {
            model.addAttribute("uregister","注册失败");
            return 0;
        }*/
    }

    //=================================商家注册=============================
    @RequestMapping("/register/mregister")
    public String mregistry(BjfMerchant bjfMerchant, MultipartFile[] eImage, Model model){
        int insert;
        String url = null;//文件的url地址
        try{
            url = imgFileUploadUtill.imgUpload(eImage[0]);
            bjfMerchant.setMcMessage(url);
            url = imgFileUploadUtill.imgUpload(eImage[1]);
            bjfMerchant.setMcLicense(url);
            insert = bjfMerchantService.insertBjfmer(bjfMerchant);
            if(insert == 1){
                model.addAttribute("mregistry","注册成功");
                return "success";
            }else{
                model.addAttribute("mregistry","注册失败");
                return "false";
            }
        }catch (IOException e){
            e.printStackTrace();
            model.addAttribute("mregistry","注册失败");
            return "false";
        }
    }


}
