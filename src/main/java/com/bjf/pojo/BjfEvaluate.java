package com.bjf.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * bjf_evaluate
 * @author 
 */
@Data
@Accessors(chain = true)
public class BjfEvaluate implements Serializable {
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

    /**
     * 商家回复时间
     */
    private Date eMtime;

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

    /**
     * 评论追加图片
     */
    private String eBackImages;


    private static final long serialVersionUID = 1L;
}