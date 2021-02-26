package com.bjf.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 阿里云滑块验证
 */
@Component
public class AfsCheckUtil {
    //ACCESS_KEY、ACCESS_SECRET请替换成您的阿里云accesskey和secret
    private String ACCESS_KEY = NumberUtil.getKEYID();
    private String ACCESS_SECRET = NumberUtil.getSECRET();

    /**
     *sessionId
     * @param sessionId 为h5从风控服务器获取的返回值(字符串)，具体格式：sessionId<-->sig<-->token<-->scene
     * @param req
     * @return
     */
    public boolean check(String sessionId, HttpServletRequest req){
        try {
            String[] afs = sessionId.split(",");

            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY, ACCESS_SECRET);
            IAcsClient client = new DefaultAcsClient(profile);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");

            AuthenticateSigRequest request = new AuthenticateSigRequest();
            request.setSessionId(afs[0]);//会话id 必填参数，从前端获取，不可更改
            request.setSig(afs[1]);// 签名串必填参数，从前端获取，不可更改
            request.setToken(afs[2]);// 请求唯一标识必填参数，从前端获取，不可更改
            request.setScene(afs[3]);// 场景标识必填参数，从前端获取，不可更改
            request.setAppKey("FFFF0N00000000009889");//应用类型标识必填参数，此处填阿里云控制台给你的appkey
            request.setRemoteIp(GetIpUtil.getIpAddr(req));// 客户端IP 必填参数，后端填写

            AuthenticateSigResponse response = client.getAcsResponse(request);
            if(response.getCode() == 100){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
