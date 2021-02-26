package com.bjf.util;

public class JsonResult<T> {

    private Integer state; // 状态
    private String message; // 错误信息
    private T data; // 数据

    public JsonResult() {
    }

    public JsonResult(String message) {
        this.message = message;
    }

    public JsonResult(Integer state) {
        this.state = state;
    }

    public JsonResult(Throwable ex) {
        this.message = ex.getMessage();
    }

    public JsonResult(Integer state, T data) {
        this.state = state;
        this.data = data;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
