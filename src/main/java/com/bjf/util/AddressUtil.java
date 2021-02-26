package com.bjf.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * https://restapi.amap.com/v3/geocode/geo?key=fe2a173dd379940717296b04ca747525&address 高德地图地址
 * key=fe2a173dd379940717296b04ca747525 手动注册得到
 * */
@Component
public class AddressUtil {

    public String getLngLat(String address) {
        StringBuffer json = new StringBuffer();
        try {
//            //restapi.amap.com/v3/geocode/geo?key=您的key&address=高新区凌云产业园&city=宁波
//            "https://restapi.amap.com/v3/geocode/geo?address="+address+"&output=XML&key=fe2a173dd379940717296b04ca747525"
            URL u = new URL("https://restapi.amap.com/v3/geocode/geo?key=fe2a173dd379940717296b04ca747525&address="+address+"&city=宁波");
            URLConnection yc = u.openConnection();
            //读取返回的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(),"UTF-8"));
            String inputline = null;
            while((inputline=in.readLine())!=null){
                json.append(inputline);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonStr=json.toString();
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        //判断输入的位置点是否存在
        if(jsonObject.getJSONArray("geocodes").size()>0){
            return jsonObject.getJSONArray("geocodes").getJSONObject(0).get("location").toString();
        }

        return null;
    }

}
