package com.bjf.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/9/9
 * \* Time: 14:09
 * \* Description:
 * \
 */
@Data
public class BjfEvaluateVo implements Serializable {
    /**
     * 主键
     */
    private Integer eId;

    /**
     * 内容
     */
    private String eContent;

    /**
     * 图片
     */
    private String eImage;

    /**
     * 好评/差评/（0/1/)
     */
    private Integer eGrepStatus;

    /**
     * 连接商品
     */
    private Integer cmdId;

    /**
     * 连接用户
     */
    private Integer uId;

    /**
     * 用户评价时间
     */
    private Date eUtime;
    private String Utime;

    /**
     * 商家回复时间
     */
    private Date eMtime;
    private String Mtime;

    /**
     * 商家回复
     */
    private String eMback;

    /**
     * 用户追加
     */
    private String eUback;

    /**
     * 用户追加评价时间
     */
    private Date eUbackTime;
    private String UbackTime;
    /**
     * 评论追加图片
     */
    private String eBackImages;


    /**
     * 昵称
     */
    private String uAccount;

    /**
     * 头像
     */
    private String uHeadSculpture;
}
