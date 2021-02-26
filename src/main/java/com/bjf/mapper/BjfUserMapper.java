package com.bjf.mapper;

import com.bjf.pojo.BjfUser;
import com.bjf.pojo.BjfUserPhoneOrNumberUp;
import com.bjf.pojo.vo.BjfUserVo;
import org.apache.ibatis.annotations.Param;

public interface BjfUserMapper {
    //=========================zhd
    //通过用户名获取用户
    BjfUser queryBjfUserByName(String username);
    //添加一条用户信息
    int insertBjfUser(BjfUser user);
    //验证用户名
    String queryBjfUserName(String uusername);
    //验证手机号
    String queryUserPhoneNumber(String phonenumber);
    //验证邮箱号
    String queryUserEmail(String uemail);
    //手机号登录
    BjfUser queryBjfUserByPhoneNumber(String phoneNumber);
    //邮箱号登录
    BjfUser queryBjfUserByEmail(String email);
    BjfUserVo selectByPrimaryKey(Integer uId);
    //==================================zhd end
    Integer updateByPrimaryKeySelective(BjfUser bjfUser);
//    修改邮箱
    Integer updateMailByPrimaryKey(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp);
//    修改手机号
    Integer updateNumberByPrimaryKey(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp);

    String queryBjfUserHeadImgByPrimaryKey(Integer uId);
    //修改密码
    int updatePassword(@Param("password") String password, @Param("pwd") String pwd,@Param("uid") Integer uid);

    /**
     * 更改用户的会员状态
     * @param uMember
     * @param uId
     * @return
     */
    int updateByuIdKey(@org.apache.ibatis.annotations.Param("uMember") Integer uMember, @org.apache.ibatis.annotations.Param("uId") Integer uId);
    BjfUser selectByPrimaryKey1(Integer uId);

    int deleteByPrimaryKey(Integer uId);

    int insertSelective(BjfUser record);

    void reduceIntegral(Integer uId);

    String selectUserPasdByPrimaryKey(Integer uId);

    BjfUser findByUId(Integer uId);

    void updateIntegral(@Param("uId") Integer uId, @Param("integral") Integer integral);

    //    清空默认地址
    Integer clearUserDefaultAddress(Integer uId);
    //    设置默认地址
    Integer setUserDefaultAddress(@Param("uId") Integer uId,@Param("adId") Integer adId);
}