package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * bjf_slideshow
 * @author 
 */
@Data
public class BjfSlideshow implements Serializable {
    /**
     * 主键
     */
    private Integer slideId;

    /**
     * 图片
     */
    private String slideImage;

    private static final long serialVersionUID = 1L;
}