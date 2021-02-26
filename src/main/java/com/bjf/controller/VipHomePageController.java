package com.bjf.controller;

import com.bjf.pojo.*;
import com.bjf.pojo.vo.BjfAllMemberVo;
import com.bjf.pojo.vo.VipHomePage;
import com.bjf.service.*;
import com.bjf.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/13
 * \* Time: 10:20
 * \* Description:会员中心的业务
 * \
 */
@RestController
@CrossOrigin
@RequestMapping("/vip")
public class VipHomePageController {


    //声明redis
    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private BjfMemberService bjfMemberService;

    @Resource
    private BjfMenberRightsService bjfMenberRightsService;

    @Resource
    private BjfCommodityService bjfCommodityService;

    @Resource
    private BjfMemberPayService bjfMemberPayService;


    /**
     * 会员中心的首页
     *
     * @return
     */
    @RequestMapping("/vipHome")
    public VipHomePage vipHome(Integer UId) {

        //声明VipHomePage实体类，这个实体类用来装所有往前端传的数据
        VipHomePage vipHomePage = new VipHomePage();

        //随机获取享受折扣的商品
        List<BjfCommodity> bjfCommodityList = bjfCommodityService.selectVipCommodity();
        vipHomePage.setBjfCommodityList(bjfCommodityList);

        //获取会员权益
        List<BjfMenberRights> bjfMenberRightsList = bjfMenberRightsService.selectAll();
        vipHomePage.setBjfMenberRightsList(bjfMenberRightsList);


        //如果用户没有登录，那么让用户去登录
        if (UId == null) {
            return vipHomePage;
        } else {
            //创建一个BjfMember来封装查到的会员表的信息
            BjfAllMemberVo bjfAllMember = bjfMemberService.selectByuId(UId);
            //判断bjfMember对象是否为空，如果不为空，则代表此用户开通过会员或者现在就是会员
            if (bjfAllMember != null) {
                //System.out.println("会员表有这个用户");
                vipHomePage.setMemberRecord(1);
                vipHomePage.setUAccount(bjfAllMember.getUAccount());
                vipHomePage.setUHeadSculpture(bjfAllMember.getUHeadSculpture());
                if (bjfAllMember.getUMember() == 1) {
                    vipHomePage.setMemberState(1);
                    Date date = bjfAllMember.getMbEtime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String eTime = sdf.format(date);
                    vipHomePage.setMbEtime(eTime);
                    vipHomePage.setMbTimes(bjfAllMember.getMbTimes());
                   // System.out.println("会员过没过期,过期时间是" + eTime);
                } else {
                    vipHomePage.setMemberState(0);
                    //System.out.println("会员过期了");
                }
            } else {
                vipHomePage.setMemberRecord(0);
                //System.out.println("会员表没有这个用户");
                

            }
            return vipHomePage;
        }

    }

    /**
     * 点击开通或者续费后显示的选择框
     *
     * @return
     */
    @RequestMapping("/bjfMemberPay")
    public List<BjfMemberPay> bjfMemberPay() {

        List<BjfMemberPay> list = bjfMemberPayService.selectAll();
        return list;
    }


    /**
     * 本来订单信息做持久化，现在不做持久化了，全部存到redis，希望以后不会再改变了，阿门  2020.09.01  14:09:52
     *
     * 移除流水号  2020.09.07 11:04:40
     *
     * @Transactional   事务注解，为了确保方法一次性成功或者失败
     */
    @RequestMapping("/vipPay")
    @Transactional
    public String vipPay(Integer mbpId, Integer UId){
        System.out.println(mbpId+"======"+UId);
        BjfOrder bjfOrder=new BjfOrder();

        BjfMemberPay bjfMemberPay=bjfMemberPayService.selectByPrimaryKey(mbpId);
        //获取当前系统时间
        Date date=new Date();

        //当前系统时间加一天
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,+1);//+1今天的时间加一天
        Date tomorrow=calendar.getTime();

            //生成订单
            // 随机生成不知道几位的订单号！
            String delid = System.currentTimeMillis() + ""
                    + new Random().nextInt(1000);

            bjfOrder.setUId(UId);
            bjfOrder.setOdTotalAmount(BigDecimal.valueOf(bjfMemberPay.getMbpMoney()));
            bjfOrder.setOdStatus(0);
            bjfOrder.setOdTime(date);
            bjfOrder.setOdExpireTime(tomorrow);
            bjfOrder.setOdDelid(delid);
            BjfOrderItem bjfOrderItem=new BjfOrderItem();
            bjfOrderItem.setOdDelid(bjfOrder.getOdDelid());
            String oiName="开通本超市"+bjfMemberPay.getMbpMonth()+"个月的会员";
            bjfOrderItem.setOiName(oiName);
            bjfOrderItem.setOiPrice(bjfOrder.getOdTotalAmount());
            bjfOrderItem.setOiNum(1);
            bjfOrderItem.setOiSupport(0);

            bjfOrder.setBjfOrderItem(bjfOrderItem);
            redisUtil.hset("user:" + bjfOrder.getUId(), "VIPtPayOrder:" + bjfOrder.getOdDelid(),bjfOrder);

        return bjfOrder.getOdDelid();
    }

    @RequestMapping("/deleteVipOrderRedis")
    public String deleteVipOrderRedis(Integer UId,String OdDelid){
        redisUtil.hdel("user:" + UId, "VIPtPayOrder:" + OdDelid);
        return "deleteSuccess";
    }


}
