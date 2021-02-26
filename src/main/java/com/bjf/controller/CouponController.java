package com.bjf.controller;

import com.bjf.service.BjfCouponService;
import com.bjf.service.BjfMdiscpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
public class CouponController {
    @Resource
    private BjfCouponService couPonService;
    @Resource
    private BjfMdiscpService bjfMdiscpService;



    //展示所有可领取优惠券
    @RequestMapping("/couPon/showAll")
    @ResponseBody
    public List showAll(Integer id){
        if(id == null){
            return null;
        }
        return bjfMdiscpService.showAll(id);
    }
    //领取优惠券
    @RequestMapping("/couPon/getCouPon")
    @ResponseBody
    public int getCouPon(Integer mdpId,Integer uid){
        return bjfMdiscpService.insertData(uid,mdpId);
    }
   /* @RequestMapping("/couPon/showAll")
    @ResponseBody
    public List shwoAllCouPon(){
        return couPonService.showAllCouPon();
    }*/

    /**
     * 领取优惠券
     * @param mdpId 商家优惠券id
     * @param uid   用户id
     * @return 是否领取成功0成功，1不成功
     */
  /*  @RequestMapping("/couPon/getCouPon")
    @ResponseBody
    public int getCouPon(Integer mdpId,Integer uid){
        int count  = couPonService.addCoupon(uid,mdpId);
        return count;
    }*/

}
