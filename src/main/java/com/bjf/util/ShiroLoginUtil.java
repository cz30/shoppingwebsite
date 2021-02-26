package com.bjf.util;

import com.bjf.config.shiro.ShiroUsernamePasswordToken;
import com.bjf.pojo.BjfMerchant;
import com.bjf.pojo.BjfUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//shiro登录验证
@Component
public class ShiroLoginUtil {

    //shiro用户登录验证
    public Integer bjfUserLogiin(String loginType, String username, String pwd, boolean rememberMe, HttpSession session){
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装当前用户数据
        ShiroUsernamePasswordToken shiroUsernamePasswordToken = new ShiroUsernamePasswordToken(username,pwd,loginType,rememberMe);
        try{
            subject.login(shiroUsernamePasswordToken);
            BjfUser bjfUser = (BjfUser)subject.getPrincipal();
            //更新用户登录时间,也可以在shiroRealm里面做
            session.setAttribute("bjfUser",bjfUser);
            return bjfUser.getUId();
        }catch (UnknownAccountException e){//用户名不存在
            System.out.println("用户名错误");
            return null;
        }catch (IncorrectCredentialsException e){//密码错误
            System.out.println("密码错误");
            return null;
        }
    }

    //shiro商家登录验证
    public Integer bjfMerchantLogin(String loginType, String username, String pwd, HttpSession session){
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装当前用户数据
        ShiroUsernamePasswordToken shiroUsernamePasswordToken = new ShiroUsernamePasswordToken(username,pwd,loginType,false);
        try{
            subject.login(shiroUsernamePasswordToken);
            BjfMerchant bjfMerchant = (BjfMerchant)subject.getPrincipal();
            //更新商家登录时间
            session.setAttribute("bjfMerchant",bjfMerchant);
            return 1;
        }catch (UnknownAccountException e){//用户名不存在
            System.out.println("账号错误");
            return 0;
        }catch (IncorrectCredentialsException e){//密码不正确
            System.out.println("密码不正确");
            return 0;
        }
    }


}
