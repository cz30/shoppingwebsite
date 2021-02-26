package com.bjf.pojo.vo;

import com.bjf.pojo.BjfCoupon;
import com.bjf.util.TimeChangeUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class BjfCouponVo  implements Serializable {

    public BjfCouponVo() {
    }

    public BjfCouponVo(BjfCoupon bjfCoupon) {
        this.cnId = bjfCoupon.getCnId();
        this.cnName = bjfCoupon.getCnName();
        this.cnDetal =bjfCoupon.getCnDetal();
        this.cnUsetime = TimeChangeUtil.changeTime(bjfCoupon.getCnUsetime(),"yyyy.MM.dd");
        this.cnExpire = TimeChangeUtil.changeTime(bjfCoupon.getCnExpire(),"yyyy.MM.dd");
        this.cnValue = bjfCoupon.getCnValue();
        this.cnLowValue = bjfCoupon.getCnLowValue();
        this.cnStatus = bjfCoupon.getCnStatus();
        this.uId = bjfCoupon.getUId();
    }

    /**
     * id主键
     */
    private Integer cnId;

    /**
     * 优惠券名称
     */
    private String cnName;

    /**
     * 使用条件
     */
    private Integer cnDetal;

    /**
     * 使用时间
     */
    private String cnUsetime;

    /**
     * 到期时间
     */
    private String cnExpire;

    /**
     * 优惠券价值
     */
    private Integer cnValue;

    /**
     * 最低使用值
     */
    private Integer cnLowValue;

    /**
     * 使用状态：0待使用，1已过期
     */
    private Integer cnStatus;

    /**
     * 用户id
     */
    private Integer uId;

    private static final long serialVersionUID = 1L;
}
