package com.bjf.controller;

import com.bjf.config.AlipayConfig;
import com.bjf.pojo.*;
import com.bjf.service.*;
import com.bjf.util.HttpClient;
import com.bjf.util.RedisUtil;
import com.bjf.util.XMLUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/24
 * \* Time: 12:48
 * \* Description:会员中心的支付模块
 * \
 */
@Controller
@RequestMapping("/vippay")
public class VipPaymentController {

    // 服务号Id
    @Value("${appid}")
    private String appid;
    // 商户号Id
    @Value("${partner}")
    private String partner;
    // 密钥
    @Value("${partnerkey}")
    private String partnerkey;

    @Resource
    private BjfOrderService bjfOrderService;

    @Autowired
    AlipayClient alipayClient;

    @Resource
    private BjfPaymentService bjfPaymentService;

    @Resource
    private BjfOrderItemService bjfOrderItemService;

    @Resource
    private BjfMemberService bjfMemberService;

    @Resource
    private BjfUserService bjfUserService;

    //声明redis
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/alipay/submit")
    @ResponseBody
    public String submitPayment(Integer UId, String Delid) throws Exception {
        BjfOrder order= (BjfOrder) redisUtil.hget("user:" + UId, "VIPtPayOrder:" + Delid);

        BjfOrderItem items = order.getBjfOrderItem();

        BjfPayment bjfPayment=new BjfPayment();
        bjfPayment.setPayOutTradeNo(order.getOdDelid());
        bjfPayment.setPayTotalAmount(order.getOdTotalAmount());

        String paySubject=items.getOiName();
        bjfPayment.setPaySubject(paySubject);
        bjfPayment.setPayWay(0);
        bjfPayment.setPayPaymentStatus(0);
        bjfPayment.setPayCreateTime(new Date());

        redisUtil.set("VIPBjfPayment:" + bjfPayment.getPayOutTradeNo(),bjfPayment,7200);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl("http://47.96.110.63:8989/vippay/alipay/callback/return");
        alipayRequest.setNotifyUrl("http://47.96.110.63:8989/vippay/alipay/callback/notify");
        // 声明一个Map
        //交易内容
        Map<String,Object> bizContnetMap=new HashMap<>();
        bizContnetMap.put("out_trade_no",bjfPayment.getPayOutTradeNo());
        bizContnetMap.put("product_code","FAST_INSTANT_TRADE_PAY");
        bizContnetMap.put("subject",bjfPayment.getPaySubject());
        bizContnetMap.put("total_amount",bjfPayment.getPayTotalAmount());
        bizContnetMap.put("passback_params",String.valueOf(UId));
        // 将map变成json
        String Json = JSON.toJSONString(bizContnetMap);
        alipayRequest.setBizContent(Json);
        String form="";

        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单

        AlipayTradePagePayResponse response=alipayClient.pageExecute(alipayRequest);
        if(response.isSuccess()){
            System.out.println("成功");
        }else{
            System.out.println("失败");
        }
        return form;
    }

    //同步回调
    @RequestMapping("/alipay/callback/return")
    public String callbackReturn() {
        return "redirect:" + AlipayConfig.return_order_url;
    }

    // 异步回调
    @RequestMapping("/alipay/callback/notify")
    @ResponseBody
    public String callbackNotify(@RequestParam Map<String,String> paramMap){

        boolean flag = false; //调用SDK验证签名
        try {
            flag = AlipaySignature.rsaCheckV1(paramMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        if(flag){
            System.err.println("支付宝回调签名认证成功！");
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            //  校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            // 对业务的二次校验
            // 只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功。
            // 支付成功之后，需要做什么？
            // 需要得到trade_status
            String trade_status = paramMap.get("trade_status");
            // 通过out_trade_no 查询支付状态记录
            String out_trade_no = paramMap.get("out_trade_no");

            Integer UId = Integer.valueOf(paramMap.get("passback_params"));

            if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)){
                BjfOrder order  = (BjfOrder) redisUtil.hget("user:" + UId, "VIPtPayOrder:" + out_trade_no);

                if (order == null) {
                    return "failure";
                }
                // 当前的订单支付状态如果是已经付款，或者是关闭
                if (order.getOdStatus() != 0) {
                    return "failure";
                }
                // 支付成功：order与order里的orderItem进行持久化保存！并删除缓存中等待过期的订单！
                bjfOrderService.insertSelective(order);
                BjfOrderItem items = order.getBjfOrderItem();
                bjfOrderItemService.insertSelective(items);
                BjfPayment bjfPayment= (BjfPayment)redisUtil.get("VIPBjfPayment:" +out_trade_no);
                bjfPayment.setPayCallbackTime(new Date());
                bjfPayment.setPayCallbackContent("success");
                bjfPayment.setPayPaymentStatus(1);
                bjfPaymentService.savePayment(bjfPayment);
                redisUtil.hdel("user:" + UId, "VIPtPayOrder:" + out_trade_no);
                redisUtil.del("VIPBjfPayment:" + out_trade_no);
                //会员部分的业务逻辑


                //把月份提取出来（从字符串中截取数字）
                String regEx="[^0-9]";
                Pattern p=Pattern.compile(regEx);
                Matcher m=p.matcher(items.getOiName());
                int month= Integer.parseInt(m.replaceAll("").trim());
                //获取当前系统时间（会员开始时间）

                //计算会员结束时间
                Calendar calendar=Calendar.getInstance();

                //计算配送次数
                int freeDistribution= (int) redisUtil.get("freeDeliveryTimes");
                int mbTimes=freeDistribution*month;

                //更改用户表的会员状态
                BjfMember bjfMember=new BjfMember();
                bjfMember.setUId(UId);
                //创建一个BjfMember来封装查到的会员表的信息
                BjfMember bjfMember1 = bjfMemberService.selectByUIdKey(UId);
                BjfUser bjfUser = bjfUserService.selectByPrimaryKey1(UId);
                if(bjfUser.getUMember()==1){//是会员，对会员表进行update
                    //System.out.println("是会员，对会员表进行update");
                    bjfMember.setMbStime(bjfMember1.getMbStime());
                    calendar.setTime(bjfMember1.getMbEtime());
                    calendar.add(Calendar.MONTH,month);
                    bjfMember.setMbEtime(calendar.getTime());
                    bjfMember.setMbTimes(bjfMember1.getMbTimes()+mbTimes);
                    bjfMemberService.updateByPrimaryKeySelective(bjfMember);
                }else{//不是会员
                    //System.out.println("不是会员");
                    Date date=new Date();
                    calendar.setTime(date);
                    calendar.add(Calendar.MONTH,month);
                    bjfMember.setMbStime(date);
                    bjfMember.setMbEtime(calendar.getTime());
                    bjfMember.setMbTimes(mbTimes);
                    if(bjfMember1 == null){
                        //System.out.println("会员表没有这条信息，进行insert");
                       bjfMemberService.insertSelective(bjfMember);
                    }
                    else{
                        //System.out.println("会员表有这条信息，进行update");
                        bjfMemberService.updateByPrimaryKeySelective(bjfMember);
                    }
                    bjfUserService.updateByuIdKey(1,UId);
                }

                System.out.println("回调成功！！！！！！！！！！");
                return "success";
            }
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            return "failure";
        }
        return "failure";
    }

    @RequestMapping("wx/submit")
    @ResponseBody
    public Map wxSubmit(Integer UId, String Delid) {
        BjfOrder order= (BjfOrder) redisUtil.hget("user:" + UId, "VIPtPayOrder:" + Delid);
        if(order==null){
            return null;
        }
        BjfOrderItem item = order.getBjfOrderItem();

        //1.创建参数
        //setScale(2, BigDecimal.ROUND_HALF_UP)   这个是四舍五入到小数点后两位
        BigDecimal totalAmount = order.getOdTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        String moneyNew = String.valueOf(Math.round(Double.parseDouble(String.valueOf(totalAmount))*100));

        BjfPayment bjfPayment=new BjfPayment();
        bjfPayment.setPayOutTradeNo(Delid);
        bjfPayment.setPayTotalAmount(order.getOdTotalAmount());
        String paySubject=item.getOiName();
        bjfPayment.setPaySubject(paySubject);
        bjfPayment.setPayWay(1);
        bjfPayment.setPayPaymentStatus(1);
        bjfPayment.setPayCreateTime(new Date());
        redisUtil.set("VIPBjfPayment:" + bjfPayment.getPayOutTradeNo(),bjfPayment,7200);

        Map<String,String> param = new HashMap();//创建参数
        param.put("appid", appid);//公众号
        param.put("mch_id", partner);//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        param.put("body", paySubject);//商品描述
        param.put("out_trade_no", Delid);//商户订单号
        param.put("total_fee",moneyNew);//总金额（分）
        param.put("spbill_create_ip", "127.0.0.1");//IP
        param.put("notify_url", "http://47.96.110.63:8989/vippay/wx/notify");//回调地址
        param.put("trade_type", "NATIVE");//交易类型
        param.put("attach", String.valueOf(UId));// 附加数据,偷渡uId


        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            // 导入工具类：项目中
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 设置https 请求
            httpClient.setHttps(true);
            // 将xmlParam 发送到接口上
            httpClient.setXmlParam(xmlParam);
            // 以post 请求
            httpClient.post();

            // 获取结果：将结果集放入map 中！
            Map<String, String> resultMap=new HashMap<>();
            // 将结果集转换为map
            String result  = httpClient.getContent();
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(result);
            resultMap.put("code_url",xmlToMap.get("code_url"));
            resultMap.put("nonce_str", xmlToMap.get("nonce_str"));
            resultMap.put("appid", appid);//公众号
            resultMap.put("sign", xmlToMap.get("sign"));
            resultMap.put("trade_type", "NATIVE");
            resultMap.put("out_trade_no",Delid);
            resultMap.put("attach", String.valueOf(UId));
            resultMap.put("mch_id", partner);//商户号
            resultMap.put("body", paySubject);//商品描述
            resultMap.put("total_fee", moneyNew);//总金额（分）
            resultMap.put("spbill_create_ip", "127.0.0.1");//本机IP
            resultMap.put("notify_url", "http://47.96.110.63:8989/vippay/wx/notify");//回调地址
            // 将结果返回控制器
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }


    @RequestMapping("/wx/notify")
    @ResponseBody
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 读取参数
        StringBuffer sb = new StringBuffer();
        InputStream inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> map = new HashMap<>();
        map = XMLUtil.doXMLParse(sb.toString());

        // 过滤器 设置TreeMap
        SortedMap packageParams = new TreeMap();
        packageParams.put("sign", map.get("sign"));
        packageParams.put("attach", map.get("attach"));
        packageParams.put("body", "宁波邦基帆科技有限公司");
        packageParams.put("nonce_str", map.get("nonce_str"));
        packageParams.put("out_trade_no", map.get("out_trade_no"));
        packageParams.put("spbill_create_ip", "123.207.218.73");
        packageParams.put("total_fee", map.get("total_fee"));
        packageParams.put("trade_type", map.get("trade_type"));
        packageParams.put("appid", appid);
        packageParams.put("mch_id", partner);
        packageParams.put("notify_url", "http://47.96.110.63:8989/vippay/wx/notify");
        //if (WXPayUtil.isSignatureValid(packageParams, partnerkey)) {
        String resXml = "";
        if ("SUCCESS". equals(String.valueOf(map.get("result_code")))) {
            // 支付成功的业务逻辑
            String mch_id = map.get("mch_id");
            String openid = map.get("openid");
            String is_subscribe = map.get("is_subscribe");
            String out_trade_no = map.get("out_trade_no");
            String total_fee = map.get("total_fee");


            Integer UId = Integer.valueOf(map.get("attach"));
            BjfOrder order  = (BjfOrder) redisUtil.hget("user:" + UId, "VIPtPayOrder:" + out_trade_no);
            if(order == null){
                return "failure";
            }
            if(order.getOdStatus()!=0){
                return "failure";
            }
            // 支付成功：order与order里的orderItem进行持久化保存！并删除缓存中等待过期的订单！
            bjfOrderService.insertSelective(order);
            BjfOrderItem items = order.getBjfOrderItem();
            bjfOrderItemService.insertSelective(items);
            BjfPayment bjfPayment= (BjfPayment)redisUtil.get("VIPBjfPayment:" +out_trade_no);
            bjfPayment.setPayCallbackTime(new Date());
            bjfPayment.setPayCallbackContent("success");
            bjfPayment.setPayPaymentStatus(1);
            bjfPaymentService.savePayment(bjfPayment);
            redisUtil.hdel("user:" + UId, "VIPtPayOrder:" + out_trade_no);
            redisUtil.del("VIPBjfPayment:" + out_trade_no);
            //会员部分的业务逻辑


            //把月份提取出来（从字符串中截取数字）
            String regEx="[^0-9]";
            Pattern p=Pattern.compile(regEx);
            Matcher m=p.matcher(items.getOiName());
            int month= Integer.parseInt(m.replaceAll("").trim());
            //获取当前系统时间（会员开始时间）

            //计算会员结束时间
            Calendar calendar=Calendar.getInstance();

            //计算配送次数
            int freeDistribution= (int) redisUtil.get("freeDeliveryTimes");
            int mbTimes=freeDistribution*month;

            //更改用户表的会员状态
            BjfMember bjfMember=new BjfMember();
            bjfMember.setUId(UId);
            //创建一个BjfMember来封装查到的会员表的信息
            BjfMember bjfMember1 = bjfMemberService.selectByUIdKey(UId);
            BjfUser bjfUser = bjfUserService.selectByPrimaryKey1(UId);
            if(bjfUser.getUMember()==1){//是会员，对会员表进行update
                //System.out.println("是会员，对会员表进行update");
                bjfMember.setMbStime(bjfMember1.getMbStime());
                calendar.setTime(bjfMember1.getMbEtime());
                calendar.add(Calendar.MONTH,month);
                bjfMember.setMbEtime(calendar.getTime());
                bjfMember.setMbTimes(bjfMember1.getMbTimes()+mbTimes);
                bjfMemberService.updateByPrimaryKeySelective(bjfMember);
            }else{//不是会员
                //System.out.println("不是会员");
                Date date=new Date();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH,month);
                bjfMember.setMbStime(date);
                bjfMember.setMbEtime(calendar.getTime());
                bjfMember.setMbTimes(mbTimes);
                if(bjfMember1 == null){
                    //System.out.println("会员表没有这条信息，进行insert");
                    bjfMemberService.insertSelective(bjfMember);
                }
                else{
                    //System.out.println("会员表有这条信息，进行update");
                    bjfMemberService.updateByPrimaryKeySelective(bjfMember);
                }
                bjfUserService.updateByuIdKey(1,UId);
            }
            //System.err.println("支付信息已保存！");

            //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

        } else {
            System.err.println("支付失败,错误信息：" + packageParams.get("err_code"));
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<retun_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return "failure";
        }

        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
        /*} else{
            System.err.println("通知签名验证失败");
            return "failure";
        }*/
        return "success";
    }
    @RequestMapping("wx/pay/status")
    @ResponseBody
    public Integer payStatus(String Delid) {
        BjfOrder bjfOrder = bjfOrderService.selectByPrimaryKey(Delid);
        if (bjfOrder == null) {
            return 0;
        }
        return bjfOrder.getOdStatus();
    }
}