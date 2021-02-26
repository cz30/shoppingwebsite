package com.bjf.service.impl;

import com.bjf.enums.ResoultEnum;
import com.bjf.mapper.BjfAddressMapper;
import com.bjf.pojo.BjfAddress;
import com.bjf.service.BjfAddressService;

import com.bjf.service.BjfUserService;
import com.bjf.service.ex.InsertException;
import com.bjf.service.ex.UpdateException;
import com.bjf.util.AddressUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * ResoultEnum  枚举类
 *   OK： 200  成功
 *     ERR： 500  失败
 *       OUT: 400   地址超区
 *
 *
 * */
@Service
@Transactional
public class BjfAddressServiceImpl implements BjfAddressService {

    @Resource
    private AddressUtil addressUtil;

    @Resource
    private BjfAddressMapper addressMapper;

    @Resource
    private  BjfUserService bjfUserService;


    @Override
    public List<BjfAddress> selectByKey(Integer userId) {

        if (userId != null) {
            List<BjfAddress> list = addressMapper.selectByKey(userId);
            return list;
        }
        return null;
    }

    @Override
    public Integer deletByAdId(Integer adId,Integer uId) {
        if (adId != null) {
            Integer resoult = addressMapper.deleteByPrimaryKey(adId,uId);
            if (resoult > 0) {
                return ResoultEnum.OK.getIndex();
            }
        }
        return ResoultEnum.ERR.getIndex();
    }


    @Override
    public int insertOrUpDataAddress(BjfAddress bjfAddress) {
        int resoult;
        if (bjfAddress != null) {
            if (bjfAddress.getAdId() != null) {
                resoult = addressMapper.updateByPrimaryKeySelective(bjfAddress);
                return   resoult>0?  ResoultEnum.OK.getIndex(): ResoultEnum.ERR.getIndex();
            }
            resoult = addressMapper.insertSelective(bjfAddress);
          return   resoult>0?  ResoultEnum.OK.getIndex(): ResoultEnum.ERR.getIndex();
        }
        return ResoultEnum.ERR.getIndex();
    }

    @Override
    public  String  addressCoordinates(String address){
        String Coordinates=null;
        if (address!=null && address!=""){
            Coordinates= addressUtil.getLngLat(address);
        }
        return Coordinates;
    }

    @Override
    public BjfAddress getOneAddress(Integer aid) {
        return addressMapper.selectByPrimaryKey(aid);
    }

    @Override
    public List<BjfAddress> getAddressList(Integer uId) {
        return addressMapper.selectAddressList(uId);
    }

    @Override
    public Integer addNewAddress(BjfAddress address) {
        Integer rows = addressMapper.insertSelective(address);
        Integer adId = address.getAdId();
        if (rows != 1) {
            throw new InsertException("新增地址失败！");
        }
        return adId;
    }

    @Override
    public void changeAddress(BjfAddress address) {
        Integer rows = addressMapper.updateByPrimaryKeySelective(address);
        if (rows != 1) {
            throw new UpdateException("修改地址失败！");
        }
    }

    //    删除地址
    @Override
    public Integer deleteAddress(Integer adId) {
        if (adId!=null){
            return  addressMapper.deleteUserAddress(adId);
        }
        return null;
    }


    // 判断逻辑
    @Override
    public Integer deleteAddressByUserId(Integer uId,Integer adId, boolean type){
        if (uId != null){
            if(type){
//                删除默认地址记录
                Integer clearDefaultAddress=bjfUserService.clearUserDefaultAddress(uId);
//                删除地址信息
                Integer deleteResout=deleteAddress(adId);
                if (clearDefaultAddress!=null && clearDefaultAddress >0){
                    if (deleteResout!= null && deleteResout>0){
                        return deleteResout;
                    }
                }
            }
            Integer deleteResout=deleteAddress(adId);
            return deleteResout;
        }
        return null;
    }
}
