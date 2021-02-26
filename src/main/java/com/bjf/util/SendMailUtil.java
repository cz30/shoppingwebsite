package com.bjf.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class SendMailUtil {
    private static final String USER = com.bjf.util.NumberUtil.getMailUser(); // 发件人称号，同邮箱地址
    private static final String PASSWORD = com.bjf.util.NumberUtil.getMailPassword(); // 如果是qq邮箱可以使户端授权码，或者登录密码

    @Resource
    private GetCodeAndCheckUtil randomCode;//四位随机数
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    private String CODE;
    private String TITLE = com.bjf.util.NumberUtil.getTITLE();//邮件标题

    private String TEXT = com.bjf.util.NumberUtil.getTEXT();//邮件内容



    /**
     *
     * @param to 收件人邮箱
     * @param text 邮件正文
     * @param title 标题
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String text, String title){
        //text = NumberUtil.getTEXT();


        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.163.com");

            // 发件人的账号
            props.put("mail.user",USER);
            //发件人的密码
            props.put("mail.password", PASSWORD);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);

            // 设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 设置邮件标题
            message.setSubject(title);

            // 设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * mailId 邮箱号
     * */
    public boolean sendMail(String mailId){
        CODE = randomCode.code();
        //存入redis
        redisTemplate.opsForValue().set(mailId,CODE,1, TimeUnit.MINUTES);
        boolean mail = sendMail(mailId,CODE+TEXT,TITLE);
        return mail;
    }
}
