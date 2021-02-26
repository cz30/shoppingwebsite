package com.bjf.service;

import com.bjf.pojo.BjfUser;
import com.bjf.pojo.BjfUserPhoneOrNumberUp;
import com.bjf.pojo.vo.BjfUserVo;
import org.apache.ibatis.annotations.Param;

public interface BjfUserService {
    //根据用户名查找用户
    BjfUser queryBjfUserByName(String username);
    //添加一条用户
    int insertBjfUser(BjfUser user);
    //验证用户名
    Integer queryBjfUserName(String u_username);
    //验证手机号
    Integer queryUserPhoneNumber(String phonenumber);
    //验证邮箱号
    Integer queryUserEmail(String uemail);
    //通过手机号登录
    BjfUser queryBjfUserByPhoneNumber(String phoneNumber);
    //通过邮箱登录
    BjfUser queryBjfUserByEmail(String email);

    BjfUserVo selectByPrimaryKey(Integer uId);
    Integer updateByPrimaryKeySelective(BjfUser bjfUser);
    String queryBjfUserHeadImgByPrimaryKey(Integer uId);

    //    修改邮箱
    Integer updateMailByPrimaryKey(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp);
    //    修改手机号
    Integer updateNumberByPrimaryKey(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp);
    //修改密码
    int updatePassword(String password,String pwd,Integer uid);
    boolean updataUserPassWord(Integer uId,String oldPasd,String newPasd,String mPasd);
    int updateByuIdKey(@Param("uMember") Integer uMember, @Param("uId") Integer uId);

    BjfUser selectByPrimaryKey1(Integer uId);

    Integer getIntegral(Integer uId);

    boolean getMember(Integer uId);

    void subIntegral(Integer uId);

    BjfUser getByUId(Integer uId);

    void defaultAddress(Integer uId, Integer adId);

    void addIntegral(Integer uId, Integer integral);

    //    删除默认地址
    Integer clearUserDefaultAddress(Integer uId);
    //    设置默认地址
    Integer setUserDefaultAddress(Integer uId,Integer adId);
}
