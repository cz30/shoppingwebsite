package com.bjf.util;


import com.bjf.pojo.vo.BjfOrderVo;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//时间更换
@Service
public  class TimeChangeUtil {

    private static SimpleDateFormat sdf = null;

    //订单业务层
    public static List<BjfOrderVo> changeTime(List<BjfOrderVo> list, String strDate){
        sdf = new SimpleDateFormat(strDate);
        for (BjfOrderVo bjfOrderVo : list) {
            bjfOrderVo.setOdTimeStr(sdf.format(bjfOrderVo.getOdTime()));
        }
        return list;
    }

    public static List<BjfOrderVo> changeTime(List<BjfOrderVo> list){
        String strDate = "yyyy-MM-dd HH:mm:ss";
        sdf = new SimpleDateFormat(strDate);
        for (BjfOrderVo bjfOrderVo : list) {
            if(bjfOrderVo.getOdTime() != null){
                bjfOrderVo.setOdTimeStr(sdf.format(bjfOrderVo.getOdTime()));
            }
            if(bjfOrderVo.getOdExpireTime() != null){
                bjfOrderVo.setOdExpireTimeStr(sdf.format(bjfOrderVo.getOdExpireTime()));
            }
            if(bjfOrderVo.getOdPayTime() != null){
                bjfOrderVo.setOdPayTimeStr(sdf.format(bjfOrderVo.getOdPayTime()));
            }
            if(bjfOrderVo.getOdModifiedTime() != null){
                bjfOrderVo.setOdModifiedTimeStr(sdf.format(bjfOrderVo.getOdModifiedTime()));
            }
        }
        return list;
    }

    public static String changeTime(Date date,String timeStr){
        sdf = new SimpleDateFormat(timeStr);
        if (date!=null)
        return sdf.format(date);
        return null;
    }


    public static Date changeTime(String date,String timeStr){
        sdf = new SimpleDateFormat(timeStr);

        try{
            if (date!=null)
                return sdf.parse(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
