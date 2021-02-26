package com.bjf.util;


import com.bjf.pojo.BjfUserPhoneOrNumberUp;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public  class SendCodeUtil {

    @Resource
    private   SendMailUtil sendMailUtil;

    @Resource
    private    SendMessageUtil sendMessageUtil;



//    private static class returnUtil{
//        private static SendCodeUtil S=new SendCodeUtil();
//    }
//
//
//
////    获取单例对象
//    public static SendCodeUtil getSendCodeUtil(){
//        return returnUtil.S;
//    }

    /*
     *
     *      修改手机或邮箱
     *         根据getType（） 决定验证方式
     *           发送对应的验证码
     *
     * */

    public  void sendCode(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp) {
        if (bjfUserPhoneOrNumberUp != null) {
            switch (bjfUserPhoneOrNumberUp.getType()) {
                case "mail":
                    try {
                        sendMailUtil.sendMail(bjfUserPhoneOrNumberUp.getOldMail());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "number":
                    try {
                        sendMessageUtil.send(bjfUserPhoneOrNumberUp.getOldNumber());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
