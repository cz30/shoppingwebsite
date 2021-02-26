package com.bjf.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * bjf_menber_rights
 * @author 
 */
@Data
public class BjfMenberRights implements Serializable {
    /**
     * 主键
     */
    private Integer mrId;

    /**
     * 会员权益内容
     */
    private String mrContent;

    private static final long serialVersionUID = 1L;
}