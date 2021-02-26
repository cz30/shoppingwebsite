package com.bjf.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroUsernamePasswordToken extends UsernamePasswordToken {
    //当前用户登录类型
    private String userType;
    private boolean rememberMe;

    public String getUserType() {
        return userType;
    }

    /*public void setUserType(String userType) {
        this.userType = userType;
    }*/
    public ShiroUsernamePasswordToken(String u_username, String u_pwd, String userType,boolean rememberMe){
        super(u_username,u_pwd,rememberMe);
        //System.out.println(5);
       // System.out.println(u_username+u_pwd+userType);
       // System.out.println("token"+u_username + u_pwd + userType +rememberMe);
        this.userType = userType;
    }
}
