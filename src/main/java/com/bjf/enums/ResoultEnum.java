package com.bjf.enums;

/**
 *  返回值枚举类
 *
 * */
public enum ResoultEnum {
    OK(200,"成功"),ERR(500,"失败"),OUT(400,"地址超区");
    private Integer index;
    private String  message;

    ResoultEnum(Integer index, String message) {
        this.index = index;
        this.message = message;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "index=" + index +
                ", message='" + message + '\'' +
                '}';
    }
}
