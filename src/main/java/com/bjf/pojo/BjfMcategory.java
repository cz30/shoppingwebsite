package com.bjf.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * bjf_mcategory
 * @author 
 */
@Data
public class BjfMcategory implements Serializable {
    private Integer mcgId;

    /**
     * 规格表id
     */
    private Integer cgId;

    /**
     * 退货到期时间
     */
    private Double mcgTime;

    private static final long serialVersionUID = 1L;
}