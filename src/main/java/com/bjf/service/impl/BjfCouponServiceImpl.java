package com.bjf.service.impl;

import com.bjf.mapper.BjfCouponMapper;
import com.bjf.mapper.BjfMcouponMapper;
import com.bjf.mapper.BjfMdiscpMapper;
import com.bjf.pojo.BjfCoupon;
import com.bjf.service.BjfCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BjfCouponServiceImpl implements BjfCouponService {
    @Resource
    private BjfMdiscpMapper mdiscpMapper; //商家优惠券表
    @Resource
    private BjfMcouponMapper mcouponMapper;//优惠券中间表
    @Resource
    private BjfCouponMapper bjfCouponMapper;//用户优惠券表

    //删除优惠券
    @Override
    public Integer deleteCoupon(Integer cnId) {
        return bjfCouponMapper.deleteCoupon(cnId);
    }



    /**
     * 展示所有的优惠券
     * @return
     */
    @Override
    public List showAllCouPon() {
        return mdiscpMapper.selectAll();
    }

    /**
     * 领取优惠券
     * @param uid  用户id
     * @param mdpId  优惠券id
     * @return  是否领取成功0成功，1不成功
     */
    @Override
    public int addCoupon(Integer uid, Integer mdpId) {
        if(mcouponMapper.selectCouPon(uid,mdpId) == null){
            int count = mcouponMapper.insertCoupon(mdpId,uid);
            if(count == 1){
                return 0;
            }
        }else if(mcouponMapper.selectCouPon(uid,mdpId) != null){
            return 1;
        }
        return 1;
    }


    /**
     *  根据用户id 获取所有优惠券
     * @param uId  用户id
     * @return
     */

    @Override
    public List<BjfCoupon> selectByUid(Integer uId) {
        if (uId!=null){
            return bjfCouponMapper.selectByUid(uId);
        }
        return null;
    }

    @Override
    public BjfCoupon getById(Integer cnId) {
        return bjfCouponMapper.selectByPrimaryKey(cnId);
    }

    @Override
    public void changeCoupon(BjfCoupon coupon) {
        bjfCouponMapper.updateByPrimaryKeySelective(coupon);
    }

}
