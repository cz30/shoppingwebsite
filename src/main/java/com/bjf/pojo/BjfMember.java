package com.bjf.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * bjf_member
 * @author 
 */
@Data
public class BjfMember implements Serializable {
    /**
     * 主键  
     */
    private Integer mbId;

    /**
     * 用户id
     */
    private Integer uId;

    /**
     * 会员开始
     */
    private Date mbStime;

    /**
     * 会员结束
     */
    private Date mbEtime;

    /**
     * 会员剩余免费配送次数
     */
    private Integer mbTimes;

    private static final long serialVersionUID = 1L;
}