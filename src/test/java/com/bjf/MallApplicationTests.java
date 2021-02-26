package com.bjf;


import com.alibaba.fastjson.JSON;
import com.bjf.mapper.BjfCouponMapper;
import com.bjf.mapper.BjfOrderMapper;
import com.bjf.pojo.BjfCart;
import com.bjf.pojo.BjfCoupon;
import org.junit.jupiter.api.Test;
//import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static java.util.concurrent.TimeUnit.MINUTES;

@SpringBootTest
class MallApplicationTests {

    /*@Resource(name= "stringRedisTemplate")
    private HashOperations<String,String,String> opsForHash;*/

    @Test
    void test20(){
        System.out.println("hello");
    }

/*    @Resource
    private BjfAddressServerImpl addressServer;

    @Resource
    private SendMessageUtil sendMessageUtil;

    @Resource
    private  SendMailUtil sendMailUtil;

    @Resource
    private BjfUserService bjfUserService;

    @Resource
    private ImgFileUploadUtill imgFileUploadUtill;

    @Resource
    private BjfCouponServer bjfCouponServer;

    @Resource
    private BjfOrderItemMapper itemMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    BjfOrderMapper orderMapper;*/

    @Test
    void jsontest(){
//        BjfCart bjfCart2=new BjfCart();
//        System.out.println(JSON.toJSONString(bjfCart2));
        String str="{cartId=null, cartImage=http://192.168.0.200/group1/M00/00/06/wKgAyF9gHlyAA5hJAADPD1cyfEc345.jpg, cartOprice=56.00, cartNprice=56.00, cartTitle=不好喝的科罗娜, cartNum=1, uId=100, cmdId=77, cartTime=Tue Sep 29 15:25:03 CST 2020, spfId=3, cartContent={尺码:L,颜色:白}, spfCount=7}";
        BjfCart bjfCart= JSON.parseObject(str,BjfCart.class);
        System.out.println(bjfCart);
    }
/*    @Test
    void contextLoads() {
        BjfUserVo bjfUser=bjfUserService.selectByPrimaryKey(1);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(dateformat.format(bjfUser.getUBirth()));
    }

    @Test
    void test1(){
//        System.out.println(bjfUserService.queryBjfUserByPhoneNumber("1111111"));
//        imgFileUploadUtill.deleteimg("http://192.168.0.200/group1/M00/00/01/wKgAyF8aVT-APKzBAADIw6_R4OU864.jpg");
//        imgFileUploadUtill.imgUpload("E:\\work\\idea\\workspace\\website\\src\\main\\resources\\static\\images")
//        List<BjfCoupon> list=bjfCouponServer.selectByUid(1);

//        if (list!=null){
//            return list;
//        }

    //        String time=TimeChangeUtil.changeTime(list.get(3).getCnUsetime(),"yyyy.MM.dd");
//        System.out.println("time:"+time);
//        System.out.println("data:"+TimeChangeUtil.changeTime(time,"yyyy.MM.dd"));
//        System.out.println(JSON.toJSONString(list));

//        短信验证
//        sendMessageUtil.send("15268865644");

//        sendMailUtil.sendMail("1367878505@qq.com");
//try {
//    System.out.println(redisTemplate.opsForValue().get("1367878505"));
//}catch (Exception e){
//    e.printStackTrace();
//    System.out.println("bucunzai");
//}

//        BjfUserPhoneOrNumberUp bjfUserPhoneOrNumberUp=new BjfUserPhoneOrNumberUp();
//        bjfUserPhoneOrNumberUp.setUId(1);
//        bjfUserPhoneOrNumberUp.setNumber("13758841472");
//
//        System.out.println(bjfUserService.updateNumberByPrimaryKey(bjfUserPhoneOrNumberUp));

//        redisTemplate.opsForValue().set("hahah","word");
        System.out.println(redisTemplate.opsForValue().get("lala"));
    }

    @Resource
    BjfOrderMapper orderMapper;
    @Test
    void test8(){
        System.out.println(orderMapper.selectOderItem("1597886410483508"));
    }
    @Test
    void  test9() throws ParseException {
//        redisTemplate.opsForHash().put("k1","k2","k3");0
        *//*redisTemplate.opsForHash().put("k1","k3","k3");
        redisTemplate.opsForHash().put("k1","k4","k3");
        redisTemplate.opsForHash().put("k1","k5","k3");
     Set list = redisTemplate.opsForHash().keys("k1");*//*
        //redisTemplate.opsForValue().set("k1",2,2, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("k1",2,10,TimeUnit.SECONDS);
        redisTemplate.opsForHash().put("h1","k2",redisTemplate.opsForValue().get("k1"));




    }
    @Test
    void test11(){
        redisTemplate.delete("k1");
    }

    @Test
    void test10(){
        System.out.println(redisTemplate.opsForHash().get("h1","k2"));
    }
    @Test
    void test12(){
        redisTemplate.opsForValue().set("finishOrder:1596439944581","1596439944581");
        redisTemplate.opsForValue().set("finishOrder:1596760887347","1596760887347");
        redisTemplate.opsForValue().set("finishOrder:1597889245524807","1597889245524807");
        redisTemplate.opsForValue().set("finishOrder:1597910664070986","1597910664070986");
        redisTemplate.opsForValue().set("finishOrder:1597911725152117","1597911725152117");
        redisTemplate.opsForValue().set("finishOrder:1596528033620","1596528033620");
        redisTemplate.opsForValue().set("order:1597909932519854","1597909932519854");
        redisTemplate.opsForValue().set("order:1597910245810979","1597910245810979");
        redisTemplate.opsForValue().set("order:1597911210660118","1597911210660118");
        redisTemplate.opsForValue().set("order:1597911451807881","1597911451807881");
        redisTemplate.opsForValue().set("order:1597911712458226","1597911712458226");
        redisTemplate.opsForValue().set("order:159791173633570","159791173633570");
        redisTemplate.opsForValue().set("order:1597911779415248","1597911779415248");
        redisTemplate.opsForValue().set("order:1597911792549404","1597911792549404");
        redisTemplate.opsForValue().set("backOrder:1596438440098","1596438440098");
        redisTemplate.opsForValue().set("backOrder:1597886410483508","1597886410483508");

    }
    @Test
    void test13(){
        redisTemplate.opsForValue().set("k1",orderMapper.selectOderItem("1596438440098"),60,TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("k1"));
    }*/

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    BjfOrderMapper orderMapper;
    @Test
    void test14(){
        //System.out.println(orderMapper.selectGetOrder("1596438440098"));

       // redisTemplate.opsForHash().put("back","ll",12);
        //redisTemplate.expire("back",1, MINUTES);//30分钟后过期
       // System.out.println(redisTemplate.opsForHash().get("back","ll"));
       // redisTemplate.opsForValue().set("user:"+1,orderMapper.selectAll(1,0,10),5, MINUTES);
       // System.out.println(redisTemplate.opsForValue().get("user:"+1));
        //JSONObject jsonObject = new JSONObject(redisTemplate.opsForHash().entries("user:" + 1));
        //System.out.println(redisTemplate.opsForValue().get("user:"+1));
        //System.out.println(jsonObject.toJSONString());
        redisTemplate.opsForValue().set("13486697492","3324",30, MINUTES);
    }

    @Resource
    BjfCouponMapper couponMapper;
    @Test
    void  test18(){
        BjfCoupon bjfCoupon = new BjfCoupon();
        bjfCoupon.setUId(1);
        Integer count = couponMapper.insertConpon(bjfCoupon);
        Integer cnid = bjfCoupon.getCnId();
        System.out.println(count);
        System.out.println(cnid);
    }

    @Test
    public void test() {
        System.err.println("Hello World!");
    }


}
