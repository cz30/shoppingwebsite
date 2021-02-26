package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * bjf_category
 * @author 
 */
@Data
public class BjfCategory implements Serializable {
    /**
     * 主键
     */
    private Integer cgId;

    /**
     * 类目名称
     */
    private String cgName;

    /**
     * 父类目录id
     */
    private List<BjfCategory> next;

    private static final long serialVersionUID = 1L;
}