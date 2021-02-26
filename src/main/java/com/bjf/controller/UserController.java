package com.bjf.controller;


import com.bjf.enums.ResoultEnum;
import com.bjf.pojo.BjfCoupon;
import com.bjf.pojo.BjfUser;
import com.bjf.pojo.BjfUserPhoneOrNumberUp;
import com.bjf.pojo.vo.BjfCouponVo;
import com.bjf.pojo.vo.BjfUserVo;
import com.bjf.service.BjfCouponService;
import com.bjf.service.BjfMemberService;
import com.bjf.service.BjfUserService;
import com.bjf.util.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 个人中心控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private BjfUserService bjfUserService;

    @Resource
    private ImgFileUploadUtill imgFileUploadUtill;

    @Resource
    private BjfCouponService bjfCouPonService;

    //    redis
    @Resource
    private RedisTemplate<Object, String> redisTemplate;

    //    会员中心类
    @Resource
    private BjfMemberService bjfMemberService;


//   获取验证码对象
    @Resource
    private SendCodeUtil sendCodeUtil;

    /*
     *  根据用户ID 获取所有用户信息
     * */
    @RequestMapping("/getUser")
    public BjfUserVo getUser(Integer uId) {
        if (uId != null && uId > 0) {
            BjfUserVo vo = bjfUserService.selectByPrimaryKey(uId);
            if (vo != null && vo.getUBirth() != null) {
//                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                vo.setSuBirth(TimeChangeUtil.changeTime(vo.getUBirth(), "yyyy-MM-dd"));
            }
//            设置用户会员状态
            vo.setUMember(bjfMemberService.selectCountByUserId(uId));
            return vo;
        }
        return null;
    }

    /*
     *   修改用户信息
     *
     *       MultipartFile  图片文件对象
     *
     *       String birth  日期有前台传入为string类型 转换成data类型在存储
     * */
    @RequestMapping("/updataUser")
    public Integer updataUser(BjfUser bjfUser, MultipartFile file, String birth) {
        String imgPath = null;
        if (bjfUser != null) {
//            上传新图片
            if (file != null) {
//            得到旧的图片地址 删除图片
//            得到原始用户信息
                String oldImgPath = bjfUserService.queryBjfUserHeadImgByPrimaryKey(bjfUser.getUId());
                if (oldImgPath != null && oldImgPath.trim().length() > 0) {
                    imgFileUploadUtill.deleteimg(oldImgPath);
                }
                try {
                    imgPath = imgFileUploadUtill.imgUpload(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bjfUser.setUHeadSculpture(imgPath);
            }
            if (birth != null && birth.trim().length() > 0 && !"--".equals(birth)) {
//                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date uBirth = null;
                try {
                    uBirth = TimeChangeUtil.changeTime(birth, "yyyy-MM-dd");
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResoultEnum.ERR.getIndex();
                }
                bjfUser.setUBirth(uBirth);
            }
            Integer resoult = bjfUserService.updateByPrimaryKeySelective(bjfUser);
            if (resoult != null) {
                return ResoultEnum.OK.getIndex();
            }
            return ResoultEnum.ERR.getIndex();
        }
        return ResoultEnum.ERR.getIndex();
    }


    //    根据用户Id 获取该用户的所有优惠券
    @PostMapping("/getUserCouPon")
    public List<BjfCouponVo> getUserCouPon(Integer uId) {
        List<BjfCoupon> list = bjfCouPonService.selectByUid(uId);
        List<BjfCouponVo> voList = new ArrayList<BjfCouponVo>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                voList.add(new BjfCouponVo(list.get(i)));
            }
            return voList;
        }
        return null;
    }


    /*
     *
     *      修改手机或邮箱
     *
     * */
    @PostMapping("/updataPhoneOrNumber")
    public void updataPhoneOrNumber(BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp) {
        sendCodeUtil.sendCode(bjfUserPhoneOrNumberUp);
    }

    /***
     *
     * @param code   验证码
     * @param type   判断验证类型对象
     * @return
     *
     *   根据bjfUserPhoneOrNumberUp 从redis中饭获取验证码
     *      与传入的验证码code 对比 返回 true or false
     *
     */
    @RequestMapping("/verify")
    public Integer verify(String code, String type, BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp) {
//        存储于redis中的验证码
        String security = null;
//       修改数据库的返回值
        Integer resoult = null;
//        获取验证码
        if (type != null && bjfUserPhoneOrNumberUp!=null) {
            switch (type) {
                case "mail":
                    try {
                        security = redisTemplate.opsForValue().get(bjfUserPhoneOrNumberUp.getOldMail());
                    } catch (Exception e) {
                        security = null;
                    }

                    break;
                case "number":
                    try {
                        security = redisTemplate.opsForValue().get(bjfUserPhoneOrNumberUp.getOldNumber());
                    } catch (Exception e) {
                        security = null;
                    }
                    break;
            }
        }
//        判断验证码
        if (security == null || "".equals(security)) {
            return ResoultEnum.ERR.getIndex();
        }

//        修改数据库数据
        if (code.equals(security)) {
            if (bjfUserPhoneOrNumberUp.getNumber() != null && bjfUserPhoneOrNumberUp.getNumber().trim().length()>0) {
                resoult = bjfUserService.updateNumberByPrimaryKey(bjfUserPhoneOrNumberUp);
                return resoult > 0 ?  ResoultEnum.OK.getIndex() :  ResoultEnum.ERR.getIndex();
            } else {
                resoult = bjfUserService.updateMailByPrimaryKey(bjfUserPhoneOrNumberUp);
                return resoult > 0 ?  ResoultEnum.OK.getIndex() :  ResoultEnum.ERR.getIndex();
            }
        }
        return ResoultEnum.ERR.getIndex();
    }



    @RequestMapping("/updataUserPassWord")
    public boolean updataUserPassWord(Integer uId,String oldPasd,String newPasd,String mPasd){

        return  bjfUserService.updataUserPassWord(uId,oldPasd,newPasd,mPasd);
    }


}
