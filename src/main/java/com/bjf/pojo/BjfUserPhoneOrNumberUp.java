package com.bjf.pojo;

import lombok.Data;

/**
 *
 *   验证修改手机或者邮箱
 *
 * */
@Data
public class BjfUserPhoneOrNumberUp {
//    验证码类型
    private String type;

//    邮箱号
    private String mail;
//    手机号
    private String number;

    //    旧邮箱号
    private String oldMail;
    //    旧手机号
    private String oldNumber;

    private  Integer uId;

    @Override
    public String toString() {
        return "BjfUserPhoneOrNumberUp{" +
                "type='" + type + '\'' +
                ", mail='" + mail + '\'' +
                ", number='" + number + '\'' +
                ", oldMail='" + oldMail + '\'' +
                ", oldNumber='" + oldNumber + '\'' +
                ", uId=" + uId +
                '}';
    }
}
