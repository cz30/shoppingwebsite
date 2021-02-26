package com.bjf.config.shiro;

import com.bjf.pojo.BjfMerchant;
import com.bjf.service.impl.BjfMerchantServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

public class BusyRealm extends AuthorizingRealm {
    @Resource
    private BjfMerchantServiceImpl merchantService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //System.out.println("busy用户授权");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();

        BjfMerchant busy = (BjfMerchant) subject.getPrincipal();

        //System.out.println(busy);

        info.addStringPermission(busy.getMcPerms());



        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //System.out.println("busy用户认证....");

        ShiroUsernamePasswordToken busyToken = (ShiroUsernamePasswordToken) token;

        BjfMerchant busy = merchantService.queryBjfMerchantByName(busyToken.getUsername());
        System.out.println(busy);
        if(busy == null){
            return null;
        }

        //如果用户登录就在session中存值 没有就不存 用于前端页面判断
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser",busy);

        return new SimpleAuthenticationInfo(busy,busy.getMcPwd(),"");
    }
}
