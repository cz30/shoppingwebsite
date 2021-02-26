package com.bjf.service.impl;

import com.bjf.mapper.*;
import com.bjf.pojo.*;
import com.bjf.pojo.vo.BjfOrderVo;
import com.bjf.service.*;
import com.bjf.service.ex.InsertException;
import com.bjf.util.GetPageUtil;
import com.bjf.util.TimeChangeUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;


import static java.util.concurrent.TimeUnit.MINUTES;

//订单表业务
@Service
public class BjfOrderServiceImpl implements BjfOrderService {

    /*@Resource
    private RedisTemplate template;*/

    @Resource
    private BjfOrderMapper orderMapper;

    @Resource
    private BjfOrderItemMapper itemMapper;

    @Resource
    private BjfOrderItemService itemService;

    @Resource
    private BjfCartService cartService;

    @Resource
    private BjfAddressService addressService;

    @Resource
    private BjfUserService userService;

    @Resource
    private BjfCommodityService commodityService;

    @Resource
    private BjfSpfService spfService;

    @Resource
    private BjfCouponService couponService;

    @Resource
    private BjfMemberService memberService;

    @Resource
    private BjfMerchantService merchantService;

    @Resource
    private BjfPaymentMapper bjfpaymentMapper;
    @Resource
    private GetPageUtil getPageUtil;//获得总页数
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private BjfBackmentMapper bjfBackmentMapper;
    @Resource
    private BjfEvaluateMapper bjfEvaluateMapper;

    /* --------------------------创建订单开始-------------------------- */

    @Override
    @Transactional
    public BjfOrder createOrder(Integer uId, Integer adId,
                              Boolean integralChecked,
                              Integer cnId, Boolean useMbTimes,
                              String message) {
        System.err.println("\tOrderServiceImpl.saveOrder()执行...");
        String delid = "" + System.nanoTime() + new Random().nextInt(1000);
        BjfAddress address = addressService.getOneAddress(adId);
        Date now = new Date();
        List<BjfOrderItem> orderItems = new ArrayList<>();
        List<BjfCart> carts = cartService.getCartCheckedList(uId);
        // 遍历购物车数据，判断是否有库存！
        for (BjfCart cart : carts) {
            BjfSpf spf = spfService.getBySpfId(cart.getSpfId());
            if (spf.getSpfCount() < cart.getCartNum()) {
                return null;
            }
            // 声明一个订单明细对象
            BjfOrderItem item = new BjfOrderItem();

            item.setOdDelid(delid);
            item.setCmdId(cart.getCmdId());
            item.setOiName(cart.getCartTitle());
            item.setOiImage(cart.getCartImage());
            item.setOiPrice(spf.getSpfPrice());
            item.setOiNum(cart.getCartNum());
            item.setOiContent(cart.getCartContent());
            item.setSpfId(cart.getSpfId());
            item.setOiSupport(commodityService.
                    getOneByCmdId(item.getCmdId()).
                    getCmdSupport());

            // 遍历查找当前商品是否参与会员打折，
            BjfCommodity commodity = commodityService.
                    getOneByCmdId(item.getCmdId());
            // svipPrice为临时参数，用来存储会员价！
            BigDecimal svipPrice = new BigDecimal("0");
            Integer status = commodity.getCmdCountStatus();
            // 如果该件商品为打折商品
            if (status == 1) {
                // 取得此商品的折扣值
                BigDecimal discount = commodity.getCmdDiscount();
                // 会员价为真实单价*折扣！
                svipPrice = spfService.getBySpfId(
                        item.getSpfId()).getSpfPrice().multiply(discount);
            }
            item.setSvipPrice(svipPrice);

            orderItems.add(item);
            itemService.addOneItem(item);
        }
        BjfOrder order = new BjfOrder();
        // orderItems为临时参数，用来存储订单明细！
        order.setOrderItems(orderItems);
        order.setUId(uId);
        if (message != null && message != "") {
            order.setOdMessage(message);
        }

        // 判断是否有会员价
        boolean isSvip = userService.getMember(uId);
        BigDecimal totalAmount = new BigDecimal("0");
        if (isSvip) {
            // 如果这B是会员就让他享受一下会员价
            for (BjfOrderItem orderItem : orderItems) {
                totalAmount = totalAmount.add(orderItem.getSvipPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getOiNum())));
            }
        } else {
            // 不是会员就原价
            for (BjfOrderItem orderItem : orderItems) {
                totalAmount = totalAmount.add(orderItem.getOiPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getOiNum())));
            }
        }
        BigDecimal cmdFee = merchantService.getMerchant(1).getMcCmdfee();

        // 从商家表找到统一配送费，赋值给dpfee
        BigDecimal dpfee = new BigDecimal("0");
        Integer mbTimes = memberService.getMPByUId(uId);
        // 对配送费做进一步判断
        if (totalAmount.compareTo(cmdFee) < 0) {
            if (isSvip) {
                if (mbTimes > 0 && useMbTimes) {
                    dpfee = BigDecimal.valueOf(0);
                    memberService.reduceMbTimes(uId);
                }
            } else {
                dpfee = merchantService.getDpfeeByMcId(1);
            }
        }

        order.setMcDpfee(dpfee);
        // 此时的总价为商品总价+运费的价格
        totalAmount = totalAmount.add(dpfee);

        // 用积分totalAmount = totalAmount.subtract(integral);
        BigDecimal integral = new BigDecimal("0");
        // 如果为true，即为使用积分
        if (integralChecked) {
            // 调用使用积分的私有方法，赋值给当前积分值
            integral = useIntegral(uId);
        }
        // 此时的总价为商品总价+运费的价格-积分抵扣值
        totalAmount = totalAmount.subtract(integral);
        BigDecimal zero = new BigDecimal("0");
        // 判断：若是使用完积分后的总价<0，设置最低值为0
        if (totalAmount.compareTo(zero) < 0) {
            totalAmount = BigDecimal.valueOf(0);
        }
        //template.opsForValue().set("order:" + delid + ":isIntegral", "true");

        //传入的优惠券必是待使用状态，且未到期！
        //判断优惠券：通用还是满减，使用优惠券
        if (cnId != null) {
            BjfCoupon coupon = couponService.getById(cnId);
            boolean flag = coupon.getCnDetal() == 0; // true是通用；false是满减
            if (flag) {
                // 是通用
                    totalAmount = totalAmount.subtract(BigDecimal.valueOf(
                            coupon.getCnValue()));
                    if (totalAmount.compareTo(zero) < 0) {
                        totalAmount = BigDecimal.valueOf(0);
                }
            } else {
                // 否则是满减
                if (totalAmount.compareTo(BigDecimal.valueOf(
                        coupon.getCnLowValue())) >= 0) {
                    totalAmount = totalAmount.subtract(BigDecimal.valueOf(
                            coupon.getCnValue()));
                    if (totalAmount.compareTo(zero) < 0) {
                        totalAmount = BigDecimal.valueOf(0);
                    }
                }
            }
            coupon.setCnUsetime(now);
            coupon.setCnStatus(1);//更新优惠券状态为已使用！
            couponService.changeCoupon(coupon);

            order.setCnValue(coupon.getCnValue());
        }

        // 此时的总价为商品总价+运费的价格-积分抵扣值-优惠券抵扣值
        order.setOdTotalAmount(totalAmount);
        order.setOdStatus(0);
        order.setOdTime(now);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        order.setOdExpireTime(calendar.getTime());
        order.setOdDelid(delid);
        order.setOdOutTradeNo(delid);
        order.setOdRecvAddress(address.getAdAddress());
        order.setOdRecvName(address.getAdUser());
        order.setOdRevcPhone(address.getAdPhone());

        // 订单保存至数据库！
        Integer rows = orderMapper.insertSelective(order);
        if (rows != 1) {
            throw new InsertException("插入订单数据失败！,order-->" + order);
        }

        // 保存订单至缓存！
        /*String orderKey = "user:" + uId + ":order";
        String orderNotPayKey = "NotPay:" + delid;
        redisTemplate.opsForValue().set(orderNotPayKey, order);
        redisTemplate.expire(orderNotPayKey, 30, TimeUnit.MINUTES);
        opsForHash.put(orderKey, delid, JSON.toJSONString(order));*/

        //保存订单后，删除购物车数据(Redis和MySQL里的全部请空)！
        Integer[] cids = new Integer[carts.size()];
        int index = 0;
        for (BjfCart cart : carts) {
            Integer cid = cart.getCartId();
            cids[index++] = cid;
        }
        cartService.moveToCart(uId, cids);

        // 减库存！
        for (BjfOrderItem orderItem : orderItems) {
            Integer spfId = orderItem.getSpfId();
            Integer num = orderItem.getOiNum();
            spfService.reduceCount(spfId, num);
        }
        return order;
    }

    @Override
    public BjfOrder getOrderByTradeNo(String outTradeNo) {
        BjfOrder order = orderMapper.selectByDelid(outTradeNo);
        return order;
    }

    @Override
    public void changeOrderByNo(BjfOrder order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public void delExOrderByNo(String outTradeNo) {
        orderMapper.deleteByTradeNo(outTradeNo);
    }

    /**
     * 用积分
     * @return
     */
    private BigDecimal useIntegral (Integer uId) {
        Integer integer = userService.getIntegral(uId);
        if (integer == null) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal integral = new BigDecimal(integer/ 100);
        return integral;
    }

    /* --------------------------创建订单结束-------------------------- */

    //查询所有订单
    @Override
    public Map selectAll(Integer uid, Integer page,Integer size) {
        Integer pages = (page - 1) * size;
        int totalPage;//总页数
        String strDate = "yyyy-MM-dd HH:mm:ss";
        Map map=new HashMap();
        List<String> odlids = orderMapper.selectOrderDelid(uid,pages,size);
        Integer count = orderMapper.selectCount(uid);
        List<BjfOrderVo> lists = new ArrayList<>();
        for (String odlid : odlids) {
            BjfOrderVo vo = orderMapper.selectAll(uid,odlid);
            lists.add(vo);
        }
        //总页数
        if(page == 1){
            if(count % size == 0){
                totalPage = count / size;
            }else {
                totalPage = count / size + 1;
            }
            map.put("totalPage",totalPage);
        }


        lists = TimeChangeUtil.changeTime(lists,strDate);
        map.put("list",lists);
        map.put("oderNum",lists.size()-1);
        map.put("currentpage",page);
        return map;
    }

    //删除订单
    @Override
    public int deleteOrder(String odDelid) {
        return orderMapper.updateOderStatus(odDelid,11);
    }

    //订单状态:0-未支付，1-已支付，2-已取消，3-已关闭，4-已完成,5-退款成功，6-待退款，7-退款失败 查询订单
    @Override
    public Map selectOthers(Integer uid, Integer page, Integer size, int status) {
        String strDate = "yyyy-MM-dd HH:mm:ss";
        Map map=new HashMap();
        if(page == 1){
            map.put("totalPage",getPageUtil.getAllPage(uid,status,size));
        }
        List<BjfOrderVo> list = new ArrayList<>();
        if(status == 8){
            list = orderMapper.selectNotSay(uid,(page-1)*size,size,2);
        }else {
            list = orderMapper.selectOthers(uid, (page-1)*size, size,status);
        }
        list = TimeChangeUtil.changeTime(list,strDate);
        map.put("list",list);
        map.put("oderNum",list.size()-1);
        map.put("currentpage",page);
        return map;
    }

    //查询订单详情
    @Override
    public List selectOderItem(String odDelid,Integer status) {
        List<BjfOrderVo> list = new ArrayList<>();
        try {
            if(status == 0){
                list = orderMapper.selectNotPayOrderItem(odDelid);
            }else{
                //获取订单详情
                list = orderMapper.selectOderItem(odDelid);
            }
            list = TimeChangeUtil.changeTime(list);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return list;
    }

    //添加退款理由及退款图片
    @Override
    public int updateBackMoney(String odDelid, String reasion, StringBuffer images,Integer oiId) {
        //List<Integer> oiIdss
        List<BjfOrderItem> items = itemMapper.selectAllSupport(odDelid);//获取所有要退的商品
        //Set<Integer> oiIds = redisTemplate.opsForHash().keys(odDelid);
        //BigDecimal allMoney = new BigDecimal((String) redisTemplate.opsForValue().get("back:"+odDelid+":"+oiId)); //退款总金额
        //阿里交易编码
        String aliPay = bjfpaymentMapper.selectAliPayTrandNo(odDelid);
        Integer status = orderMapper.selectOrderStatus(odDelid); //确定商品退款的状态
        if(status == 1){
            status = 5;
        }else if(status == 9){
            status = 7;
        }

        BjfOrder order = new BjfOrder();
        BjfBackment backment = new BjfBackment();;



            try {
                BigDecimal allMoney = new BigDecimal(redisTemplate.opsForValue().get("back:"+odDelid+":"+oiId).toString()); //退款总金额
                if(allMoney != null){

                    //添加退款信息
                    backment.setBmMoney(allMoney);
                    backment.setPayOutTradeNo(odDelid);
                    backment.setOiId(oiId);
                    backment.setPayAlipayTradeNo(aliPay);
                    backment.setBmStatus(0);
                    backment.setBmReasion(reasion);
                    backment.setBmImage(images.toString());
                    backment.setBmMoney(allMoney);
                    //将数据存入redis
                    //redisTemplate.opsForValue().set("backItem:"+odDelid+":" + oiId,backment,30*60,TimeUnit.SECONDS);
                    //存入数据库
                    if(bjfBackmentMapper.insertBackOrderItems(backment)== 1){
                        //修改订单状态
                        if(itemMapper.updateOIStatus(odDelid, oiId,0) == 1){
                            redisTemplate.delete("back:"+odDelid+":"+oiId); //删除缓存
                            return 1;
                        }
                    }
                }else
                    return 0;
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }

        return 0;
    }

    //展示可退商品
    @Override
    public List showBack(String odDelid) {
        //BjfOrder order = orderMapper.selectGetOrder(odDelid);
        List<BjfOrderItem> items = itemMapper.selectAllSupport(odDelid);
        return items;
    }


    //展示退款金额
    @Override
    public BigDecimal showBackMoney(String odDelid, Integer oiId) {
        BjfOrder order = orderMapper.selectGetOrder(odDelid);
        List<BjfOrderItem> items = itemMapper.selectAllSupport(odDelid);
        BigDecimal orderMoney = order.getOdTotalAmount();//订单总额
        BigDecimal totalMony = new BigDecimal(0); //所有商品总额
        //BigDecimal dpFee = BigDecimal.valueOf(0); //可退订单配送费
        Integer cnValue = order.getCnValue() != null ? order.getCnValue() : 0; //订单优惠券价值
        BigDecimal regMoney = new BigDecimal("0");//实际金额
        Map map = new HashMap<>();
        for (BjfOrderItem item : items) {
            totalMony = totalMony.add(item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())));
        }
        try {
            /*if (order.getOdStatus() == 1)
                dpFee = order.getMcDpfee();*/
            for (BjfOrderItem item : items) {
                if (item.getOiId() == oiId) {
                    if (cnValue != 0) {
                        BigDecimal allMoney = null;
                        //item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())) 单个商品价格*数量 = 商品总价
                        //item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).divide(totalMony))) 商品总价 / 所有商品总价 = 所占比例
                        //订单总额*所占比例=商品实付价格
                        //BigDecimal.valueOf(cnValue).multiply (item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).divide(totalMony))) 优惠金额 * 所占比例 = 优惠金额
                        //item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).subtract(BigDecimal.valueOf(cnValue).multiply (item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).divide(totalMony))).add(dpFee) 商品总价-优惠金额 + 配送费 = 实际退货价格
                        allMoney = item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum()));
                        allMoney = allMoney.divide(totalMony,2,BigDecimal.ROUND_CEILING);
                        allMoney = orderMoney.multiply(allMoney);
                        //allMoney = totalMony.subtract(allMoney).add(dpFee);
                        regMoney = regMoney.add(allMoney);
                    } else {
                        //商品单价 * 总价 + 配送费
                        regMoney = regMoney.add(item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        map.put("regMoney",regMoney);//实际退款金额
        redisTemplate.opsForValue().set("back:"+odDelid+":"+oiId,regMoney,30*60,TimeUnit.SECONDS);
        return regMoney;
    }

    //确认订单
    @Override
    public int updateOderTrue(String odDelid){
        Map map = new HashMap();
        Integer uid = orderMapper.selectUserId(odDelid);
        try {
            orderMapper.updateOrderModTime(odDelid,new Date());
            //获取订单状态
            Integer status = null;
            Long hours = null;
            //获取该订单中所有商品
            List<BjfOrderItem> lists = itemMapper.selectAllSupport(odDelid);
            for (BjfOrderItem list : lists) {
                itemMapper.updateOIStatus(odDelid,list.getOiId(),3);
            }
            if(orderMapper.selectOrderStatus(odDelid) == 1) {
                if (lists != null) {
                    //到期日期
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
                    Date date = calendar.getTime();
                    String time = TimeChangeUtil.changeTime(date, "yyyy-MM-dd");
                    status = 2; //完成可退

                    return orderMapper.updateOderTrue(odDelid, status);
                } else {
                    status = 2; //完成不可退
                    for (BjfOrderItem list : lists) {
                        //添加评论表内容
                        if(bjfEvaluateMapper.insertOrderItem(uid,list.getCmdId(),0) != null)
                            return 0;
                    }
                    return orderMapper.updateOderTrue(odDelid, status);
                }
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    //取消订单
    @Override
    public int updateNotBack(String odDelid) {
        BjfOrder order = new BjfOrder();
        Integer status = null;

        if(orderMapper.selectOrderStatus(odDelid) == 5)
            status = 2;
        else if(orderMapper.selectOrderStatus(odDelid) == 7)
            status = 1;
        if(status == null){
            return 0;
        }
        order.setOdDelid(odDelid);
        order.setOdStatus(status);
        return orderMapper.updateNotBack(odDelid);
    }

    //取消退款
    @Override
    public int notBackMoney(String odDelid,Integer oiId) {
        try {
            //删除退款表中数据
            if (bjfBackmentMapper.delectBackOrderItems(odDelid,oiId) == 1){
                //修改订单状态
                if(itemMapper.updateOIStatus(odDelid,oiId,6) == 1)
                            return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

    @Override
    public int upBackItem(String odDelid, Integer oiIds) {
        return 0;
    }

   /* //提交退款
    @Override
    public int upBackItem(String odDelid, Integer oiIds) {
        BjfBackment backment = null;
        try {
            for (Object oiId : oiIds) {
                if(redisTemplate.opsForValue().get("backItem:"+odDelid+":" + oiId) != null){
                    //存入数据库
                    if(bjfBackmentMapper.insertBackOrderItems(backment)== 1){
                        //修改订单详情状态
                        if(itemMapper.updateOIStatus(odDelid,(Integer) oiId,1) == 1){
                            //删除缓存
                            redisTemplate.delete("backItem:"+odDelid+":" + oiId);
                            return 1;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
        return 0;
    }*/

    //退款商品
    public BjfOrderItem backItem(String odDelid,Integer oiId){
        BjfOrderItem bjfOrderItem = itemMapper.selectBackItem(odDelid,oiId);
        try {
            if(bjfOrderItem != null){
                BjfOrder order = orderMapper.selectGetOrder(odDelid);
                List<BjfOrderItem> items = itemMapper.selectAllSupport(odDelid);
                BigDecimal orderMoney = order.getOdTotalAmount();//订单总额
                BigDecimal totalMony = new BigDecimal(0); //所有商品总额
                //BigDecimal dpFee = BigDecimal.valueOf(0); //可退订单配送费
                Integer cnValue = order.getCnValue() != null ? order.getCnValue() : 0; //订单优惠券价值
                BigDecimal regMoney = new BigDecimal("0");//实际金额
                //Map map = new HashMap<>();
                //计算除去优惠总金额
                for (BjfOrderItem item : items) {
                    totalMony = totalMony.add(item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())));
                }
                if(cnValue != 0){
                    BigDecimal allMoney = null;
                    //item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())) 单个商品价格*数量 = 商品总价
                    //item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).divide(totalMony))) 商品总价 / 所有商品总价 = 所占比例
                    //订单总额*所占比例=商品实付价格
                    //BigDecimal.valueOf(cnValue).multiply (item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).divide(totalMony))) 优惠金额 * 所占比例 = 优惠金额
                    //item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).subtract(BigDecimal.valueOf(cnValue).multiply (item.getOiPrice().multiply(BigDecimal.valueOf(item.getOiNum())).divide(totalMony))).add(dpFee) 商品总价-优惠金额 + 配送费 = 实际退货价格
                    //allMoney = bjfOrderItem.getOiPrice().multiply(BigDecimal.valueOf(bjfOrderItem.getOiNum()));
                    //allMoney = allMoney.divide(totalMony,2,BigDecimal.ROUND_CEILING);
                    //allMoney = orderMoney.multiply(allMoney);
                    regMoney = regMoney.add(orderMoney.multiply(bjfOrderItem.getOiPrice().multiply(BigDecimal.valueOf(bjfOrderItem.getOiNum())).divide(totalMony,2,BigDecimal.ROUND_CEILING)));
                    //regMoney = regMoney.add(allMoney);
                    redisTemplate.opsForValue().set("back:"+odDelid+":"+oiId,regMoney,30*60,TimeUnit.SECONDS);
                }else {
                    //商品单价 * 总价 + 配送费
                    regMoney = regMoney.add(bjfOrderItem.getOiPrice().multiply(BigDecimal.valueOf(bjfOrderItem.getOiNum())));
                }
                bjfOrderItem.setBackMoney(regMoney);
                /*map.put("item",bjfOrderItem);
                map.put("backMoney",regMoney);*/
                return bjfOrderItem;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public int insertSelective(BjfOrder record) {
        return orderMapper.insertSelective(record);
    }
    @Override
    public BjfOrder selectByPrimaryKey(String odDelid) {
        return orderMapper.selectByPrimaryKey(odDelid);
    }
}
