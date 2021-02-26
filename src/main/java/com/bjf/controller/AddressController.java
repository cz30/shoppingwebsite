package com.bjf.controller;

import com.bjf.enums.ResoultEnum;
import com.bjf.pojo.BjfAddress;

import com.bjf.service.BjfAddressService;
import com.bjf.service.BjfUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  地址管理 ： 添加  修改   删除
 *
 *      添加： 判断地址是否超区，如没有超区则添加
 * */
@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController {

    @Resource
    private BjfAddressService addressServer;

    @Resource
    private BjfUserService bjfUserService;

//     通过用户ID获取所有地址信息
    @RequestMapping("/getAddress")
    public  List<BjfAddress> getAdressByKey(Integer uId){
       List<BjfAddress> list=addressServer.selectByKey(uId);
       return list!=null?list:null;
    }

//  删除地址  删除成功 返回执行结果
//    @RequestMapping("/deleteAddress")
//    public Integer deleteAdressByKey(Integer adid ,Integer uId){
//        int resoult=addressServer.deletByAdId(adid,uId);
//        return resoult>0?ResoultEnum.OK.getIndex():ResoultEnum.ERR.getIndex();
//    }

    /*
     *  如果存在 地址id 则修改地址信息
     *   如果不存在地址id 则添加新地址
     *      执行后返回 执行结果
     * */
    @RequestMapping("/insertOrUpData")
    public Integer insertOrUpDataAddress(BjfAddress bjfAddress){
        int resoult=0;
        if (bjfAddress!=null){
             resoult=addressServer.insertOrUpDataAddress(bjfAddress);
        }
        return resoult>0?ResoultEnum.OK.getIndex():ResoultEnum.ERR.getIndex();
    }


    /*
    * 通过地址 获取坐标
    *    将地址坐标存放到 ResoultEnum 返回前端
    *       供前端判断地址是否符合配送范围
    * */
    @PostMapping("/addressCoordinates")
    public String returnAddressCoordinates(String address){
//      if (address!=null && address!=""){
          String coordinates=addressServer.addressCoordinates(address);
//          ResoultEnum.OK.setMessage(coordinates);
          return coordinates;
//      }
//      return null;
    }

    //     设置默认地址
    @PostMapping("/setDefaultAddres")
    public Integer setDefaultAddres(Integer uId, Integer adId) {
        Integer resoult=bjfUserService.setUserDefaultAddress(uId,adId);
        return resoult!=null&&resoult>0?ResoultEnum.OK.getIndex():ResoultEnum.ERR.getIndex();
    }

    //    删除地址
    @PostMapping("/deleteAddress")
    public Integer clearUserDefaultAddress(Integer uId ,Integer adId,boolean type) {
        Integer resoult=addressServer.deleteAddressByUserId(uId,adId,type);
        return resoult>0&&resoult!=null?ResoultEnum.OK.getIndex():ResoultEnum.ERR.getIndex();
    }


}
