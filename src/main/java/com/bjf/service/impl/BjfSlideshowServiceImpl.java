package com.bjf.service.impl;


import com.bjf.mapper.BjfSlideshowMapper;
import com.bjf.pojo.BjfSlideshow;
import com.bjf.service.BjfSlideshowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/8/6
 * \* Time: 9:30
 * \* Description:
 * \
 */
@Service
@Transactional
public class BjfSlideshowServiceImpl implements BjfSlideshowService {
    @Resource
    BjfSlideshowMapper bjfSlideshowMapper;
    @Override
    public List<BjfSlideshow> slideshow() {
        return bjfSlideshowMapper.slideshow();
    }
}
