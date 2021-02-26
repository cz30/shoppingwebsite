package com.bjf.util;

import org.springframework.stereotype.Component;

@Component
public final class NumberUtil {
    //阿里云通用账户
    private  static String KEYID = "LTAI4GGx7YEA1SvuHrLJwkwM"; //账号
    private  static String SECRET = "eax3JE1GZ1eYwMTdaz1pnePtTvptQZ"; //密码

    //手机短信
    private static String TEMPLATECODE = "SMS_204605095"; //手机短信模板
    private static String SIGN_NAME = "邦基帆科技";//手机短信签名

    //邮箱
    private static String MAIL_USER = "bangjifans@163.com";//发件人邮箱账户
    private static String MAIL_PASSWORD = "BJILOOJCCLTUPUSR";//发件人邮箱账户密码
    private static String TEXT = "这是来自邦基帆商城的验证码请在1分钟内输入过期请重新领取";//邮件内容
    private static String TITLE = "商城验证码";//邮件主题

    public static String getSignName() {
        return SIGN_NAME;
    }

    public static String getMailUser() {
        return MAIL_USER;
    }

    public static String getMailPassword() {
        return MAIL_PASSWORD;
    }

    public static String getTEXT() {
        return TEXT;
    }

    public static String getTITLE() {
        return TITLE;
    }

    public static String getKEYID() {
        return KEYID;
    }

    public static String getSECRET() {
        return SECRET;
    }

    public  static String getTEMPLATECODE() {
        return TEMPLATECODE;
    }

    public static String getSIGN_NAME() {
        return SIGN_NAME;
    }
}
