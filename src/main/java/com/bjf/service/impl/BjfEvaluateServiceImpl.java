package com.bjf.service.impl;


import com.bjf.mapper.BjfEvaluateMapper;
import com.bjf.pojo.BjfEvaluate;
import com.bjf.pojo.vo.BjfEvaluateVo;
import com.bjf.service.BjfEvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/9/9
 * \* Time: 9:45
 * \* Description:
 * \
 */
@Service
public class BjfEvaluateServiceImpl implements BjfEvaluateService {
    @Resource
    BjfEvaluateMapper bjfEvaluateMapper;
    @Override
    public int insertSelective(BjfEvaluate bjfEvaluate) {
        return bjfEvaluateMapper.insertSelective(bjfEvaluate);
    }

    @Override
    public BjfEvaluate selectInitialCommentsByCmdIdAndUId(Integer eId) {
        return bjfEvaluateMapper.selectInitialCommentsByCmdIdAndUId(eId);
    }

    @Override
    public int updateAdditionalComments(BjfEvaluate bjfEvaluate) {
        return bjfEvaluateMapper.updateAdditionalComments(bjfEvaluate);
    }

    @Override
    public List<BjfEvaluateVo> selectEvaluateBycmdId(Integer cmdId) {
        return bjfEvaluateMapper.selectEvaluateBycmdId(cmdId);
    }


}
