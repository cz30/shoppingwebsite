package com.bjf.service;



import com.bjf.pojo.BjfEvaluate;
import com.bjf.pojo.vo.BjfEvaluateVo;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Administrator
 * \* Date: 2020/9/9
 * \* Time: 9:45
 * \* Description:
 * \
 */
public interface BjfEvaluateService {
    int insertSelective(BjfEvaluate bjfEvaluate);

    BjfEvaluate selectInitialCommentsByCmdIdAndUId(Integer eId);

    int updateAdditionalComments(BjfEvaluate bjfEvaluate);

    List<BjfEvaluateVo> selectEvaluateBycmdId(Integer cmdId);
}
