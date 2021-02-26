package com.bjf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjf.pojo.*;
import com.bjf.pojo.vo.OrderVO;
import com.bjf.service.*;
import com.bjf.util.ImgFileUploadUtill;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@CrossOrigin //跨域
public class OrderController {

    @Resource(name= "stringRedisTemplate")
    private HashOperations<String,String,String> opsForHash;

    @Resource
    private BjfOrderService bjfOrderService;

    @Resource
    private BjfAddressService addressService;

    @Resource
    private BjfCouponService couponService;

    @Resource
    private BjfCartService cartService;

    @Resource
    private BjfUserService userService;

    @Resource
    private BjfMemberService memberService;

    @Resource
    private BjfOrderService orderService;

    @Resource
    private BjfSpfService spfService;

    @Resource
    private BjfOrderItemService itemService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private BjfMerchantService merchantService;

    @Resource
    private BjfPaymentService paymentService;

    @Resource
    private ImgFileUploadUtill imgFileUploadUtill;

    @Resource
    private BjfCommodityService commodityService;

    /* --------------------创建订单开始--------------------- */

    @GetMapping("show/trade")
    @ResponseBody
    public OrderVO showTrade(/*HttpSession session*/Integer uId) {
        System.err.println("\tOrderController.showTrade()执行...");
        //Integer uId = (Integer) session.getAttribute("uId");
        OrderVO orderVO = new OrderVO();

        List<BjfCart> cartList = new ArrayList<>();
        String cartCheckedKey = "user:" + uId + ":cartChecked";
        List<String> stringList = opsForHash.values(cartCheckedKey);
        System.out.println("==============================");
        System.out.println(stringList);
        // 如果缓存里存在，进行遍历添加到cartList
        if (stringList != null && stringList.size() > 0) {
            for (String cartStr : stringList) {
                System.err.println("cartStr-->" + cartStr);
                cartList.add(JSON.parseObject(cartStr, BjfCart.class));
            }
        }
        for (BjfCart cart : cartList) {
            BigDecimal cmdDiscount = commodityService.getOneByCmdId(cart.getCmdId()).getCmdDiscount();
            if (cmdDiscount == null || cmdDiscount.compareTo(BigDecimal.valueOf(0)) == 0) {
                cmdDiscount = BigDecimal.valueOf(1);
                System.err.println("cmdDiscount1-->" + cmdDiscount);
            }
            System.err.println("cmdDiscount2-->" + cmdDiscount);
            cart.setDiscount(cmdDiscount);
        }
        orderVO.setCartList(cartList);
        System.err.println("cartList-->" + cartList);
        List<BjfAddress> addressList = addressService.getAddressList(uId);
        System.err.println("addressList-->" + addressList);
        orderVO.setAddressList(addressList);
        List<BjfCoupon> couponList = couponService.selectByUid(uId);
        System.err.println(couponList);
        if (couponList != null) {
            /*for (BjfCoupon coupon : couponList) {
                if (coupon.getCnStatus() != 0)
            }*/

            Iterator<BjfCoupon> it = couponList.iterator();
            // 对该用户的所有优惠券进行迭代遍历！
            while (it.hasNext()) {
                BjfCoupon coupon = it.next();

                // 定义优惠券过期时间和当前时间进行比较
                Date cnExpire = coupon.getCnExpire();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cnExpire);
                calendar1.add(Calendar.DATE, 1);
                Date now = new Date();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(now);

                // 如果优惠券状态不是可用状态或已过期，则从list中移除！
                if (coupon.getCnStatus() != 0 ||
                        !(calendar1.compareTo(calendar2) > 0)) {
                    it.remove();
                }
            }
        }
        System.err.println("couponList-->" + couponList);
        orderVO.setCouponList(couponList);
        Integer integral = userService.getIntegral(uId);
        if (integral == null) {
            integral = 0;
        }
        System.err.println("integral-->" + integral);
        orderVO.setIntegral(integral);
        Integer defaultAdId = userService.getByUId(uId).getAdId();
        System.err.println("defaultAdId-->" + defaultAdId);
        orderVO.setDefaultAdId(defaultAdId);
        boolean isMember = userService.getMember(uId);
        Integer MPTimers = memberService.getMPByUId(uId);
        orderVO.setIsMember(isMember);
        System.err.println("isMember-->" + isMember);
        if (!isMember) {
            MPTimers = null;
        }
        orderVO.setMPTimers(MPTimers);
        System.err.println("MPTimers-->" + MPTimers);
        BjfMerchant merchant = merchantService.getMerchant(1);
        BigDecimal dpFee = merchant.getMcDpfee();
        BigDecimal cmdFee = merchant.getMcCmdfee();
        orderVO.setDpFee(dpFee);
        orderVO.setCmdFee(cmdFee);
        System.err.println("配送费and免配额-->" + dpFee + "--" + cmdFee);
        System.out.println("------------------------------------");
        System.out.println(orderVO);
        return orderVO;
    }

    @RequestMapping("add/new/address")
    @ResponseBody
    public void addNewAddress(BjfAddress address) {
        Integer adId = addressService.addNewAddress(address);
        System.out.println(address);
        BjfUser user = userService.getByUId(address.getUId());
        if (user.getAdId() == null) {
            userService.defaultAddress(user.getUId(), adId);
        }

    }

    @RequestMapping("change/address")
    @ResponseBody
    public void changeAddress(BjfAddress address) {
        addressService.changeAddress(address);
    }

    @RequestMapping("default/address")
    @ResponseBody
    public void defaultAddress(Integer uId, Integer adId) {
        userService.defaultAddress(uId, adId);
    }

    @RequestMapping("create/order")
    @ResponseBody
    public String createOrder(/*HttpSession session*/Integer uId, Integer adId,
                                                     Boolean integralChecked,
                                                     Integer cnId, Boolean useMbTimes,
                                                     String message) {
        //Integer uId = (Integer) session.getAttribute("uId");
        List<BjfCart> carts = cartService.getCartCheckedList(uId);
        for (BjfCart cart : carts) {
            boolean flag = spfService.getBySpfId(cart.getSpfId()).getSpfCount() <= 0;
            if (flag) {
                return "err";
            }
        }

        // 创建订单！
        BjfOrder order = orderService.createOrder(uId, adId, integralChecked,
                cnId, useMbTimes, message);
        System.err.println(order);
        String outTradeNo = order.getOdOutTradeNo();
        System.err.println("totalAmount-->" + order.getOdTotalAmount());
        System.err.println("outTradeNo-->" + outTradeNo);
        System.err.println(order.getOdTotalAmount());
        System.err.println(order.getOdTotalAmount().compareTo(BigDecimal.valueOf(0)));
        if (order.getOdTotalAmount().compareTo(BigDecimal.valueOf(0)) == 0) {
            List<BjfOrderItem> items = order.getOrderItems();
            List<String> subject = new ArrayList<>();
            System.err.println(items);
            for (BjfOrderItem item : items) {
                subject.add(item.getOiName());
                if (item.getOiSupport() == 1) {
                    item.setOiStatus(6);
                    itemService.changeItem(item);
                }
            }
            order.setOdPayTime(new Date());
            order.setOdStatus(1);
            orderService.changeOrderByNo(order);
            BjfPayment payment = new BjfPayment();
            payment.setPayOutTradeNo(outTradeNo);
            payment.setPayAlipayTradeNo(outTradeNo);
            payment.setPayTotalAmount(order.getOdTotalAmount());
            payment.setPaySubject(String.valueOf(subject));
            payment.setPayCreateTime(new Date());
            payment.setPayWay(0);
            payment.setPayPaymentStatus(1);
            payment.setPayCallbackTime(new Date());
            payment.setPayCallbackContent("SUCCESS");
            paymentService.insertOnePayment(payment);
            System.err.println("payment===" + payment);
            BjfMerchant merchant = merchantService.getMerchant(1);
            String autoTime = String.valueOf(merchant.getMcSuccessTime());

            // 发送消息给MQ，计时自动确认收货
            rabbitTemplate.convertAndSend("orderlxExchange", "order_dlx_routs", outTradeNo, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message msg) throws AmqpException {
                    msg.getMessageProperties().setExpiration(autoTime);
                    return msg;
                }
            });
            return "0";
        }
        // 为防止用户重新勾选商品，有必要再判断一次库存！
        if (order == null) {
            return "err";
        }
        return order.getOdOutTradeNo();
    }

    /* --------------------创建订单结束--------------------- */

    //================展示全部订单==============
    @RequestMapping("/order/showAllOrder")
    @ResponseBody
    //Integer uId, Integer pag
    public Map showAllOrder(Integer uid, Integer page){

        return bjfOrderService.selectAll(uid,page,5);
    }

 /*   @RequestMapping("/order/showAllOrders")
    @ResponseBody
    //Integer uId, Integer pag
    public Map showAllOrders(Integer uid, Integer page){
        return orderService.selectAll(uid,page,10);
    }*/




    //所有订单操作 1删除 确认收货 3确认收货
    @RequestMapping("/order/operation")
    @ResponseBody
    public int orderOperation(String odDelid,int id){
        if(id == 1){
            if(bjfOrderService.deleteOrder(odDelid) == 1){
                return 1;
            }else {
                return 0;
            }
        }else if (id == 2){
            try {
                if(bjfOrderService.updateOderTrue(odDelid) == 1){
                    return 1;
                }else {
                    return 0;
                }
            }catch (Exception e){
                return 0;
            }
        }else if (id == 3){
            if(bjfOrderService.deleteOrder(odDelid) == 1){
                return 1;
            }else{
                return 0;
            }
        }

        return 0;
    }

   /* //删除订单
    @RequestMapping("/order/delereOrder")
    @ResponseBody
    public int deleteOrder(String odDelid){
        if(orderService.deleteOrder(odDelid) == 1){
            return 1;
        }else {
            return 0;
        }
    }*/

    //订单状态:0-未支付，1-已支付，2-已完成，3-已取消 4-退款成功，5已支付待退款，6-退款失败,7、已成功待退款 8、待评价  9、可退已完成
    @RequestMapping("/order/showOthers")
    @ResponseBody
    public Map showOthers(Integer uid, Integer page, int status){
        return bjfOrderService.selectOthers(uid,page,10,status);
    }


    //订单详情展示
    @RequestMapping("/order/showItem")
    @ResponseBody
    public List showItem(String odDelid,Integer status){
        System.out.println(odDelid);
        return bjfOrderService.selectOderItem(odDelid,status);
    }

    //退货商品展示
    @RequestMapping("/order/showBack")
    @ResponseBody
    public BjfOrderItem showBack(String odDelid, Integer oiId){
        return bjfOrderService.backItem(odDelid,oiId);
    }

    //退货商品金额展示
    @RequestMapping("/order/showBackMoney")
    @ResponseBody
    public BigDecimal showBackMoney(String odDelid, Integer oiId){
        return bjfOrderService.showBackMoney(odDelid,oiId);
    }

    //退货信息存入redis
    @RequestMapping("/order/backOrder")
    @ResponseBody
    public int backOrder(String odDelid, String reasion, MultipartFile[] images,Integer oiId){
        StringBuffer url = new StringBuffer();
        if(images != null){
            try{
                for(int i = 0;i < images.length;i++){
                    if ((i < images.length - 1)) {
                        url.append(imgFileUploadUtill.imgUpload(images[i]) + ",");
                    } else {
                        url.append(imgFileUploadUtill.imgUpload(images[i]));
                    }
                }
                if(bjfOrderService.updateBackMoney(odDelid,reasion,url,oiId) == 1){
                    return 1;
                }else{
                    return 0;
                }
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }else if(images == null){
            if (bjfOrderService.updateBackMoney(odDelid,reasion,null,oiId) == 1){
                return 1;
            }else {
                return 0;
            }
        }
        return 0;
    }



    //取消退货
    @RequestMapping("/order/notBack")
    @ResponseBody
    public int notBack(String odDelid,Integer oiId){
        return bjfOrderService.notBackMoney(odDelid,oiId);
    }

    /*//确认订单
    @RequestMapping("/order/takeTrue")
    @ResponseBody
    public int orderTakeTrue(String odDelid){
        if(orderService.updateOderTrue(odDelid) == 1){
            return 0;
        }else {
            return 1;
        }
    }*/


}
