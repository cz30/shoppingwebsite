package com.bjf.service.impl;

import com.bjf.mapper.BjfCouponMapper;
import com.bjf.mapper.BjfMcouponMapper;
import com.bjf.mapper.BjfMdiscpMapper;
import com.bjf.pojo.BjfCoupon;
import com.bjf.pojo.BjfMdiscp;
import com.bjf.pojo.vo.BjfMdiscpVo;
import com.bjf.service.BjfMdiscpService;
import com.bjf.util.RabbitMQUtil;
import com.bjf.util.TimeChangeUtil;
import groovy.transform.Synchronized;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BjfMdiscpServiceImpl implements BjfMdiscpService {
    @Resource
    private BjfMdiscpMapper mdiscpMapper;
    @Resource
    private BjfMcouponMapper bjfMcouponMapper;
    @Resource
    private BjfCouponMapper bjfCouponMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitMQUtil rabbitMQUtil;

    /**
     * 展示所有可领取优惠券
     * @param id 用户id
     * @return
     */
    @Override
    public List<BjfMdiscpVo> showAll(Integer id) {

        List<BjfMdiscp> mdiscps = mdiscpMapper.selectAll();
        List<BjfMdiscpVo> list = new ArrayList<>();
        String str = "yyyy-MM-dd";
        for (BjfMdiscp mdiscp : mdiscps) {
            if(mdiscp.getMdpNum() > 0 && mdiscp.getMdpSwitch() == 1){
                //查看用户是否有该优惠券
                if(bjfMcouponMapper.selectCouPon(id,mdiscp.getMdpId()) == null){
                    BjfMdiscpVo mdiscpVo = new BjfMdiscpVo();
                    mdiscpVo.setMdiscp(mdiscp);
                    mdiscpVo.setStartTime(TimeChangeUtil.changeTime(mdiscp.getMdpStartime(),str));
                    mdiscpVo.setEndTime(TimeChangeUtil.changeTime(mdiscp.getMdpEndtime(),str));
                    list.add(mdiscpVo);
                }

            }
        }
        return list;
    }



    /**
     * 领取优惠券
     * @param uid 用户id
     * @param mdpId 领取的优惠券id
     * @return
     */
    @Override
    public Integer insertData(Integer uid, Integer mdpId) {
        BjfMdiscp bjfMdiscp = mdiscpMapper.selectOne(mdpId);
        if(insertIntoCoupon(bjfMdiscp,mdpId,uid) == 1){
            if(mdiscpMapper.updateNum(mdpId) == 1){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 插入用户优惠券
     * @param bjfMdiscp 商家优惠券对象
     * @param mdpId 商家优惠券id
     * @param uid 用户id
     * @return 插入成功返回1
     */
    @Override
    public Integer insertIntoCoupon(BjfMdiscp bjfMdiscp,Integer mdpId,Integer uid){
        BjfCoupon bjfCoupon = new BjfCoupon();
        bjfCoupon.setUId(uid);
        bjfCoupon.setCnName(bjfMdiscp.getMdpName());
        bjfCoupon.setCnDetal(bjfMdiscp.getMdpType());
        bjfCoupon.setCnUsetime(bjfMdiscp.getMdpStartime());
        bjfCoupon.setCnExpire(bjfMdiscp.getMdpEndtime());
        bjfCoupon.setCnValue(bjfMdiscp.getMdpValue());
        bjfCoupon.setCnLowValue(bjfMdiscp.getMdpLowValue());
        bjfCoupon.setCnStatus(0);
        if(bjfMcouponMapper.selectCouPon(uid,mdpId) == null){
            if(bjfCouponMapper.insertConpon(bjfCoupon)== 1){
                Integer cnId = bjfCoupon.getCnId();
                bjfMcouponMapper.insertCoupon(mdpId,uid);//往领取表中插入数据
                return cnId;
            }
        }
        return 0;
    }

    /**
     * 新用户领取优惠券
     * @param uid 用户id
     * @param  mdpId 优惠券id
     * @return 领取成功1不成功0
     */
    @Override
    @Synchronized
    public Integer newUserGet(Integer uid,Integer mdpId){
        BjfMdiscp mdiscp = mdiscpMapper.selectOne(mdpId);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();//开始时间
        mdiscp.setMdpStartime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+mdiscp.getTime());
        date = calendar.getTime();
        mdiscp.setMdpEndtime(date);//到期时间
        Integer type = mdiscp.getMdpType() == 4 ? 1 : 0;
        mdiscp.setMdpType(type);
        synchronized (this){
            Integer cnId = insertIntoCoupon(mdiscp,mdiscp.getMdpId(),uid);//用户优惠券表主键id
            if(cnId != 0){
                redisTemplate.opsForHash().put("newcp","user:"+uid,mdiscp.getMdpId());
                //天数 * 24小时 * 60分钟 * 60秒
                Integer time = mdiscp.getTime() * 24 * 60 * 60 / 10 * 10000;

                rabbitMQUtil.sendTimeAndDelete(cnId,time.toString());
                return 1;
            }
        }
            return 0;
    }

    //登录优惠券
    @Override
    public Integer userLoginGet(Integer uid,Integer mdpId){
        BjfMdiscp mdiscp = mdiscpMapper.selectOne(mdpId);
        Integer type = mdiscp.getMdpType() == 2 ? 1 : 0;
        mdiscp.setMdpType(type);
        synchronized (this){
            Integer cnId = insertIntoCoupon(mdiscp,mdpId,uid);
            if(cnId != 1){
                redisTemplate.opsForHash().put("ulohinget","user:"+uid,mdiscp.getMdpId());
                Long day = ( mdiscp.getMdpEndtime().getTime()- mdiscp.getMdpStartime().getTime())/(1000 * 60 * 60 * 24);
                rabbitMQUtil.sendTimeAndDelete(cnId,day.toString());
                return 1;
            }
        }
        return 0;
    }




}
