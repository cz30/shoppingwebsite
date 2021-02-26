package com.bjf.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.bjf.config.AlipayConfig;
import com.bjf.pojo.*;
import com.bjf.service.*;
import com.bjf.util.HttpClient;
import com.bjf.util.XMLUtil;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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

/**
 * \* Description:
 * \* User: mortal
 * \* Date: 2020/9/24
 * \* Time: 13:40
 * \
 */
@CrossOrigin
@Controller
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private BjfUserService userService;

    @Autowired
    private BjfOrderService orderService;

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    private BjfPaymentService paymentService;

    @Autowired
    private BjfOrderItemService itemService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BjfMerchantService merchantService;

    @Autowired
    private BjfCommodityService commodityService;

    // 服务号Id
    @Value("${appid}")
    private String appid;
    // 商户号Id
    @Value("${partner}")
    private String partner;
    // 密钥
    @Value("${partnerkey}")
    private String partnerkey;

    @RequestMapping("alipay/submit")
    @ResponseBody
    public String submitPayment(String outTradeNo) throws AlipayApiException {
        //Integer uId = (Integer) session.getAttribute("uId");
        /*System.err.println("\tuId = " + uId);
        String orderEXKey = "NotPay:" + outTradeNo;
        Object o = redisTemplate.opsForValue().get(orderEXKey);
        if (o == null) {
            return "订单已过期，请重新下单！";
        }
        String orderKey = "user:" + uId + ":order";
        Order order = JSON.parseObject(opsForHash.get(orderKey, outTradeNo), Order.class);*/
        BjfOrder order = orderService.getOrderByTradeNo(outTradeNo);
        List<BjfOrderItem> items = itemService.getOrderItems(outTradeNo);
        List<String> subject = new ArrayList<>();
        for (BjfOrderItem item : items) {
            subject.add(item.getOiName());
        }


        BjfPayment payment = new BjfPayment();
        payment.setPayOutTradeNo(outTradeNo);
        payment.setPayAlipayTradeNo(outTradeNo);
        payment.setPayTotalAmount(order.getOdTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        payment.setPaySubject(String.valueOf(subject));
        payment.setPayCreateTime(new Date());
        payment.setPayWay(0);
        /*String paymentKey = "payment:" + outTradeNo;
        String json = JSON.toJSONString(payment);
        redisTemplate.opsForValue().set(paymentKey, json);*/
        paymentService.insertOnePayment(payment);
        System.err.println("payment===" + payment);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl("http://www.bangjifan.com:8989/alipay/callback/return");
        alipayRequest.setNotifyUrl("http://www.bangjifan.com:8989/alipay/callback/notify");

        // 声明一个Map
        Map<String, Object> bizContentMap = new HashMap<>();
        bizContentMap.put("out_trade_no", payment.getPayOutTradeNo());
        bizContentMap.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContentMap.put("subject", payment.getPaySubject());
        bizContentMap.put("total_amount", payment.getPayTotalAmount());
        //bizContentMap.put("passback_params", String.valueOf(uId));
        System.err.println("bizContentMap======" + bizContentMap);
        // 将map变成json
        String Json = JSON.toJSONString(bizContentMap);
        alipayRequest.setBizContent(Json);

        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
        if(response.isSuccess()){
            System.err.println("调用成功");
        } else {
            System.err.println("调用失败");
        }
        System.out.println(form);
        return form;
    }

    // 同步回调
    @RequestMapping("alipay/callback/return")
    public String callbackReturn() {
        return "redirect:" + AlipayConfig.return_order_url;
    }

    // 异步回调
    @RequestMapping("alipay/callback/notify")
    @ResponseBody
    public String callbackNotify(@RequestParam Map<String,String> paramMap){

        // Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        // String paramsJson = JSON.toJSONString(params);
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

            /*Integer uId = Integer.valueOf(paramMap.get("passback_params"));
            System.err.println("uId-->" + uId);*/
            // String total_amount = paramMap.get("total_amount");
            if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)){
                /*String orderKey = "user:" + uId + ":order";
                String str = opsForHash.get(orderKey, out_trade_no);
                Order redisOrder = JSON.parseObject(str, Order.class);
                if (str == null) {
                    return "failure";
                }
                // 当前的订单支付状态如果是已经付款，或者是关闭
                if (redisOrder.getOdStatus() != 0) {
                    return "failure";
                }*/

                BjfOrder order = orderService.getOrderByTradeNo(out_trade_no);
                List<BjfOrderItem> itemList = itemService.getOrderItems(out_trade_no);


                /*Object str = template.opsForValue().get("order:" + out_trade_no + ":isIntegral");
                if (str != null) {
                    userService.subIntegral(order.getUId());
                }*/
                System.err.println("order-->" + order);
                if (order == null) {
                    return "failure";
                }
                // 当前的订单支付状态如果是已经付款，或者是关闭
                if (order.getOdStatus() != 0) {
                    return "failure";
                }

                // 支付成功：order与order里的orderItem进行持久化保存！并删除缓存中等待过期的订单！
                order.setOdPayTime(new Date());
                order.setOdStatus(1);
                for (BjfOrderItem item : itemList) {
                    if (item.getOiSupport() == 1) {
                        item.setOiStatus(6);
                        itemService.changeItem(item);
                    }
                }
                Integer integral = 0;
                order.setOdPayTime(new Date());
                order.setOdStatus(1);
                for (BjfOrderItem item : itemList) {
                    if (item.getOiSupport() == 1) {
                        item.setOiStatus(6);
                        itemService.changeItem(item);
                        integral = integral + commodityService.getOneByCmdId(item.getCmdId()).getCmdScore();
                    }
                }
                orderService.changeOrderByNo(order);

                // 支付成功：order与order里的orderItem进行持久化保存！并删除缓存中等待过期的订单！
                // redisOrder.setOdPayTime(new Date());

                /*String orderNotPayKey = "NotPay:" + out_trade_no;
                if (redisTemplate.opsForValue().get(orderNotPayKey) != null) {
                    redisTemplate.delete(orderNotPayKey);
                }
                opsForHash.put(orderKey, out_trade_no, JSON.toJSONString(redisOrder));
                orderService.insertOneOrder(redisOrder);
                List<OrderItem> items = redisOrder.getOrderItems();
                for (OrderItem item : items) {
                    itemService.addOneItem(item);
                }*/

                // 更新交易记录的状态！
                /*String jsonStr = (String) redisTemplate.opsForValue().get("payment:" + out_trade_no);
                Payment payment = JSON.parseObject(jsonStr, Payment.class);*/
                BjfPayment payment = paymentService.getPayment(out_trade_no);
                payment.setPayPaymentStatus(1);
                payment.setPayCallbackTime(new Date());
                payment.setPayCallbackContent("SUCCESS");
                paymentService.updatePayment(payment);

                BjfMerchant merchant = merchantService.getMerchant(1);
                String autoTime = String.valueOf(merchant.getMcSuccessTime());

                // 发送消息给MQ，计时自动确认收货
                rabbitTemplate.convertAndSend("orderlxExchange", "order_dlx_routs", out_trade_no, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message msg) throws AmqpException {
                        msg.getMessageProperties().setExpiration(autoTime);
                        return msg;
                    }
                });

                /*redisTemplate.opsForValue().set("payment:" + out_trade_no, String.valueOf(payment));
                paymentService.insertOnePayment(payment);*/
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
    public Map wxSubmit(String outTradeNo) {
        //Integer uId = (Integer) session.getAttribute("uId");
        /*System.err.println("\tuId = " + uId);
        String orderEXKey = "NotPay:" + outTradeNo;
        Object o = redisTemplate.opsForValue().get(orderEXKey);
        if (o == null) {
            return null;
        }
        String orderKey = "user:" + uId + ":order";
        Order order = JSON.parseObject(opsForHash.get(orderKey, outTradeNo), Order.class);*/
        BjfOrder order = orderService.getOrderByTradeNo(outTradeNo);

        List<BjfOrderItem> items = itemService.getOrderItems(outTradeNo);
        List<String> subject = new ArrayList<>();
        for (BjfOrderItem item : items) {
            subject.add(item.getOiName());
        }

        //1.创建参数
        BigDecimal totalAmount = order.getOdTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        String moneyNew = String.valueOf(Math.round(Double.parseDouble(String.valueOf(totalAmount))*100));
        System.err.println("totalAmount-->" + totalAmount);

        BjfPayment payment = new BjfPayment();
        payment.setPayOutTradeNo(outTradeNo);
        payment.setPayAlipayTradeNo(outTradeNo);
        payment.setPayTotalAmount(totalAmount);
        payment.setPaySubject(String.valueOf(subject));
        payment.setPayCreateTime(new Date());
        payment.setPayWay(1);
        paymentService.insertOnePayment(payment);
        /*String paymentKey = "payment:" + outTradeNo;

        String json = JSON.toJSONString(payment);
        redisTemplate.opsForValue().set(paymentKey, json);
        System.err.println("payment-->" + payment);*/

        Map<String,String> param = new HashMap();//创建参数
        param.put("appid", appid);//公众号
        param.put("mch_id", partner);//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        param.put("body", "宁波邦基帆科技有限公司");//商品描述
        param.put("out_trade_no", outTradeNo);//商户订单号
        param.put("total_fee", moneyNew);//总金额（分）
        param.put("spbill_create_ip", "123.207.218.73");//本机IP
        param.put("notify_url", "http://www.bangjifan.com:8989/wx/notify");//回调地址(随便写)
        param.put("trade_type", "NATIVE");//交易类型
        //param.put("attach", String.valueOf(uId));// 附加数据,偷渡uId
        System.err.println("param-->" + param);

        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.err.println("xmlParam-->" + xmlParam);
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
            System.err.println("xmlToMap-->" + xmlToMap);
            resultMap.put("code_url",xmlToMap.get("code_url"));
            resultMap.put("nonce_str", xmlToMap.get("nonce_str"));
            resultMap.put("appid", appid);//公众号
            resultMap.put("sign", xmlToMap.get("sign"));
            resultMap.put("trade_type", "NATIVE");
            resultMap.put("out_trade_no",outTradeNo);
            // resultMap.put("attach", String.valueOf(uId));
            resultMap.put("mch_id", partner);//商户号
            resultMap.put("body", "宁波邦基帆科技有限公司");//商品描述
            resultMap.put("total_fee", moneyNew);//总金额（分）
            resultMap.put("spbill_create_ip", "123.207.218.73");//本机IP
            resultMap.put("notify_url", "http://www.bangjifan.com:8989/wx/notify");//回调地址(随便写)
            System.err.println("code_url-->" + resultMap.get("code_url"));
            // 将结果返回控制器
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }


    @RequestMapping("wx/notify")
    @ResponseBody
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取参数
        System.err.println("wxNotity()执行中...");
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
        System.err.println("map-->" + map);
        // 过滤器 设置TreeMap
        SortedMap packageParams = new TreeMap();
        System.err.println("packageParams-->" + packageParams);
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
        packageParams.put("notify_url", "http://www.bangjifan.com:8989/wx/notify");
        System.err.println("sign-->" + packageParams.get("sign"));
        //if (WXPayUtil.isSignatureValid(packageParams, partnerkey)) {
            String resXml = "";
            if ("SUCCESS". equals(String.valueOf(map.get("result_code")))) {
                // 支付成功的业务逻辑
                //rabbitTemplate.convertAndSend("orderExchange","redirect","OK");

                System.err.println("packageParams-->" + packageParams);
                String mch_id = map.get("mch_id");
                String openid = map.get("openid");
                String is_subscribe = map.get("is_subscribe");
                String out_trade_no = map.get("out_trade_no");
                String total_fee = map.get("total_fee");

                System.err.println("mch_id-->" + mch_id);
                System.err.println("openid-->" + openid);
                System.err.println("is_subscribe-->" + is_subscribe);
                System.err.println("out_trade_no-->" + out_trade_no);
                System.err.println("total_fee-->" + total_fee);

                /*String uId = map.get("attach");
                String orderKey = "user:" + uId + ":order";
                String str = opsForHash.get(orderKey, out_trade_no);
                Order redisOrder = JSON.parseObject(str, Order.class);*/
                BjfOrder order = orderService.getOrderByTradeNo(out_trade_no);
                List<BjfOrderItem> itemList = itemService.getOrderItems(out_trade_no);
                System.err.println("order-->" + order);
                if (order == null) {
                    return "failure";
                }
                // 当前的订单支付状态如果是已经付款，或者是关闭
                if (order.getOdStatus() != 0) {
                    return "failure";
                }

                // 支付成功：order与order里的orderItem进行持久化保存！并删除缓存中等待过期的订单！
                order.setOdPayTime(new Date());
                order.setOdStatus(1);
                for (BjfOrderItem item : itemList) {
                    if (item.getOiSupport() == 1) {
                        item.setOiStatus(6);
                        itemService.changeItem(item);
                    }
                }
                Integer integral = 0;
                order.setOdPayTime(new Date());
                order.setOdStatus(1);
                for (BjfOrderItem item : itemList) {
                    if (item.getOiSupport() == 1) {
                        item.setOiStatus(6);
                        itemService.changeItem(item);
                        integral = integral + commodityService.getOneByCmdId(item.getCmdId()).getCmdScore();
                    }
                }
                orderService.changeOrderByNo(order);

                /*String orderNotPayKey = "NotPay:" + out_trade_no;
                if (redisTemplate.opsForValue().get(orderNotPayKey) != null) {
                    redisTemplate.delete(orderNotPayKey);
                }
                opsForHash.put(orderKey, out_trade_no, JSON.toJSONString(redisOrder));
                orderService.insertOneOrder(redisOrder);
                List<OrderItem> items = redisOrder.getOrderItems();
                for (OrderItem item : items) {
                    itemService.addOneItem(item);
                }*/

                // 更新交易记录的状态！
                /*String jsonStr = (String) redisTemplate.opsForValue().get("payment:" + out_trade_no);
                Payment payment = JSON.parseObject(jsonStr, Payment.class);*/
                BjfPayment payment = paymentService.getPayment(out_trade_no);
                payment.setPayPaymentStatus(1);
                payment.setPayCallbackTime(new Date());
                payment.setPayCallbackContent("SUCCESS");
                //redisTemplate.opsForValue().set("payment:" + out_trade_no, String.valueOf(payment));
                paymentService.updatePayment(payment);

                BjfMerchant merchant = merchantService.getMerchant(1);
                String autoTime = String.valueOf(merchant.getMcSuccessTime());
                // 发送消息给MQ，计时自动确认收货
                rabbitTemplate.convertAndSend("orderlxExchange", "order_dlx_routs", out_trade_no, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message msg) throws AmqpException {
                        msg.getMessageProperties().setExpiration(autoTime);
                        return msg;
                    }
                });
                System.err.println("支付信息已保存！");

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
    public Integer payStatus(String outTradeNo) {
        BjfOrder order = orderService.getOrderByTradeNo(outTradeNo);
        if (order == null) {
            return 0;
        }
        return order.getOdStatus();
    }

}
