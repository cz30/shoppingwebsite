package com.bjf.mapper;



import com.bjf.pojo.BjfSlideshow;

import java.util.List;

public interface BjfSlideshowMapper {

    /**
     * 展示轮播图
     * @return
     */
    List<BjfSlideshow> slideshow();
}