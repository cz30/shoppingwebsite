package com.bjf.service.impl;


import com.bjf.enums.ResoultEnum;
import com.bjf.mapper.BjfUserMapper;
import com.bjf.pojo.BjfUser;
import com.bjf.pojo.BjfUserPhoneOrNumberUp;
import com.bjf.pojo.vo.BjfUserVo;
import com.bjf.service.BjfUserService;
import com.bjf.service.ex.UpdateException;
import com.bjf.util.ImgFileUploadUtill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BjfUserServiceImpl implements BjfUserService {

    @Resource
    private BjfUserMapper bjfUserMapper;

    @Resource
    private ImgFileUploadUtill imgFileUploadUtill;

    @Override
    public BjfUser queryBjfUserByName(String uUsername) {
        try{
            return bjfUserMapper.queryBjfUserByName(uUsername);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //注册添加用户
    @Override
    public int insertBjfUser(BjfUser user) {
        System.out.println(user);
        int count = bjfUserMapper.insertBjfUser(user);
        return count;
    }

    //验证用户名
    @Override
    public Integer queryBjfUserName(String uUsername) {
        try {
            if(bjfUserMapper.queryBjfUserName(uUsername) != null)
                return 0;
            else
                return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    //验证手机号
    @Override
    public Integer queryUserPhoneNumber(String phonenumber) {
        try {
            if(bjfUserMapper.queryUserPhoneNumber(phonenumber) != null){
                return 1;
            }else
                return 0;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    //验证邮箱
    @Override
    public Integer queryUserEmail(String uemail) {
        try {
            if(bjfUserMapper.queryUserEmail(uemail) != null){
                return 1;
            }else
                return 0;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public BjfUser queryBjfUserByPhoneNumber(String phoneNumber) {
        return bjfUserMapper.queryBjfUserByPhoneNumber(phoneNumber);
    }

    @Override
    public BjfUser queryBjfUserByEmail(String email) {
        return bjfUserMapper.queryBjfUserByEmail(email);
    }

    //根据用户ID查询用户信息
    @Override
    public BjfUserVo selectByPrimaryKey(Integer uId) {
        if (uId!=null && uId>0){
            BjfUserVo vo= bjfUserMapper.selectByPrimaryKey(uId);
//            if (vo!=null && vo.getUBirth()!=null){
//
//            }
          return vo;
        }
        return null;
    }

    /*
    *   修改用户信息  如果客户头像已存在  先删除图片在添加新图片
    *
    * */
    @Override
    public Integer updateByPrimaryKeySelective(BjfUser bjfUser) {
        if (bjfUser!=null){
          return   bjfUserMapper.updateByPrimaryKeySelective(bjfUser)>0? ResoultEnum.OK.getIndex(): ResoultEnum.ERR.getIndex();
        }
        return null;
    }

    @Override
    public String queryBjfUserHeadImgByPrimaryKey(Integer uId) {
        if (uId!=null){
            return bjfUserMapper.queryBjfUserHeadImgByPrimaryKey(uId);
        }
        return null;
    }

    /**
     *   修改邮箱号
     * @param bjfUserPhoneOrNumberUp
     * @return
     */
    @Override
    public Integer updateMailByPrimaryKey(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp) {
        return bjfUserMapper.updateMailByPrimaryKey(bjfUserPhoneOrNumberUp);
    }

    /**
     *  修改手机号
     * @param bjfUserPhoneOrNumberUp
     * @return
     */
    @Override
    public Integer updateNumberByPrimaryKey(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp) {
        return bjfUserMapper.updateNumberByPrimaryKey(bjfUserPhoneOrNumberUp);
    }


    /***
     *
     * @param uId       用户id
     * @param oldPasd   旧密码
     * @param newPasd   新密码
     * @param mPasd     密文密码
     * @return
     *
     *              通过用户id获取数据库密码
     *              对比旧密码正确 修改密码
     */
    @Override
    public boolean updataUserPassWord(Integer uId,String oldPasd,String newPasd,String mPasd){
//        更新数据库返回值
        Integer reoult=null;
        if (uId!=null){
//            根据用户Id查询密码
            String uPasd=bjfUserMapper.selectUserPasdByPrimaryKey(uId);
            if (uPasd!=null && oldPasd!=null){
//                对比旧密码是否相同
                if (oldPasd.equals(uPasd)){
                    BjfUser bjfUser=new BjfUser();
                    bjfUser.setUId(uId);
                    bjfUser.setUPassword(newPasd);
                    bjfUser.setUPwd(mPasd);
                    reoult=bjfUserMapper.updateByPrimaryKeySelective(bjfUser);
                }
                return ( reoult!=null && reoult>0 )?true:false;
            }
            return false;
        }
        return false;
    }



    /**
     * 修改密码
     * @param password
     * @param pwd
     * @param uid
     * @return
     */
    @Override
    public int updatePassword(String password, String pwd, Integer uid) {
        return bjfUserMapper.updatePassword(password,pwd,uid);
    }

    @Override
    public int updateByuIdKey(Integer uMember, Integer uId) {
        return bjfUserMapper.updateByuIdKey(uMember,uId);
    }

    @Override
    public BjfUser selectByPrimaryKey1(Integer uId) {
        return bjfUserMapper.selectByPrimaryKey1(uId);
    }


    @Override
    public Integer getIntegral(Integer uId) {
        Integer integer = bjfUserMapper.findByUId(uId).getUIntegral();
        return integer == null ? 0 : integer;
    }

    @Override
    public boolean getMember(Integer uId) {
        Integer member = bjfUserMapper.findByUId(uId).getUMember();
        return member == 1;
    }

    @Override
    public void subIntegral(Integer uId) {
        bjfUserMapper.reduceIntegral(uId);
    }

    @Override
    public BjfUser getByUId(Integer uId) {
        return bjfUserMapper.findByUId(uId);
    }

    @Override
    public void defaultAddress(Integer uId, Integer adId) {
        BjfUser user = new BjfUser();
        user.setUId(uId);
        user.setAdId(adId);
        Integer rows = bjfUserMapper.updateByPrimaryKeySelective(user);
        if (rows != 1) {
            throw new UpdateException("设置默认地址失败！");
        }
    }

    @Override
    public void addIntegral(Integer uId, Integer integral) {
        bjfUserMapper.updateIntegral(uId, integral);
    }


    //   清空默认地址
    @Override
    public Integer clearUserDefaultAddress(Integer uId ) {
        try {
            return  bjfUserMapper.clearUserDefaultAddress(uId);
        }catch (Exception e){
            return null;
        }

    }

    //   设置默认地址
    @Override
    public Integer setUserDefaultAddress(Integer uId, Integer adId) {
        if (uId!= null && adId != null){
             Integer resoult=bjfUserMapper.setUserDefaultAddress(uId,adId);
            return resoult;
        }
        return null;
    }


}
