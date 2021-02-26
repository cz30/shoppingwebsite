package com.bjf.pojo.vo;

import com.bjf.pojo.BjfMdiscp;
import lombok.Data;

import java.io.Serializable;

@Data
public class BjfMdiscpVo implements Serializable {
    private BjfMdiscp mdiscp;
    private Integer time;
    private String startTime;
    private String endTime;
}
