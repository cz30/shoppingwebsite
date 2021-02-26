package com.bjf.mapper;


import com.bjf.pojo.BjfEvaluate;
import com.bjf.pojo.vo.BjfEvaluateVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BjfEvaluateMapper {
    Integer insertOrderItem(@Param("uId") Integer uId,@Param("cmdId") Integer cmdId, @Param("eNotEve") Integer eNotEve);

    /**
     * 插入初次评论
     * @param bjfEvaluate
     * @return
     */
    int insertSelective(@Param("bjfEvaluate") BjfEvaluate bjfEvaluate);

    /**
     * 根据商品id和用户id查询评价表
     * @return
     */
    BjfEvaluate selectInitialCommentsByCmdIdAndUId(Integer eId);

    /**
     * 追加评价
     * @param bjfEvaluate
     * @return
     */
    int updateAdditionalComments(BjfEvaluate bjfEvaluate);

    /**
     * 商品详情页展示评论
     * @param cmdId
     * @return
     */
    List<BjfEvaluateVo> selectEvaluateBycmdId(Integer cmdId);
}