package com.bjf.util;

import com.bjf.mapper.BjfOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//获取页数
@Component
public class GetPageUtil {
    @Autowired
    private BjfOrderMapper orderMapper;

    public int getAllPage(int uid,int status,int size){
        int count;
        if(status == 8){
             count = orderMapper.selectNotSayCountByStatus(uid,2);
        }else {
            count = orderMapper.selectCountByStatus(uid,status);
        }
        int toTal;
        if(count % size == 0){
            toTal = count / size;
            return toTal;
        }else {
            toTal = count / size + 1;
            return toTal;
        }
    }

}
