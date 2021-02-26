package com.bjf.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 阿里云短信验证
 */
@Component
public class SendMessageUtil {

    @Resource
    private NumberUtil numberUtil;//秘钥

    @Resource
    private GetCodeAndCheckUtil randomCode;//生成验证码

    @Resource
    private RedisTemplate<String,String> redisTemplate;//redis

    private String ACCESS_KEY = numberUtil.getKEYID();//账号
    private String ACCESS_SECRET = numberUtil.getSECRET();//密码
    private String TEMPLATE_CODE = numberUtil.getTEMPLATECODE();//模板
    private String SIGN_NAME = numberUtil.getSIGN_NAME();//签名


    public boolean send(String phoneNumber, String templateCode) {
        if(templateCode == null){
            templateCode = TEMPLATE_CODE;
        }
        //生成验证码
        String code = null;
        code = randomCode.code();
        HashMap<String,Object> param = new HashMap<>();
        param.put("code",code);
        //判断redis中是否还有验证码
        if(redisTemplate.hasKey(phoneNumber)){
            return false;
        }
        //存入redis
        redisTemplate.opsForValue().set(phoneNumber,code,1, TimeUnit.MINUTES);
        //连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY, ACCESS_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        //构建请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");//不要动
        request.setSysVersion("2017-05-25");//不要动
        request.setSysAction("SendSms");//事件名称

        //自定义参数（手机号，验证码，签名，模板）
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);//手机号
        request.putQueryParameter("SignName", SIGN_NAME);//签名
        request.putQueryParameter("TemplateCode", templateCode);//模板
        //构建一个短信验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean send(String phoneNumber){
        return send(phoneNumber,null);
    }

}
