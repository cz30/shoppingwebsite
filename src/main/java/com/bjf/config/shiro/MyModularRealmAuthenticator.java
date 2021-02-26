package com.bjf.config.shiro;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;


/**
 *重写ModularRealmAuthenticator的子类，
 *并重写doAuthenticate()方法，通过第二步中的 userType 判断决定使用对应的Realm处理
 *即认证
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {

    private Logger logger = (Logger) LoggerFactory.getLogger(MyModularRealmAuthenticator.class);

    /**
     * 根据用户类型判断使用哪个realm
     */
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        super.assertRealmsConfigured();
        //强制转换为自定义的CustomizedToken
        ShiroUsernamePasswordToken token = (ShiroUsernamePasswordToken) authenticationToken;
        //登录类型
        String userType = token.getUserType();
        //所有realm
        Collection<Realm> Realms = getRealms();
        for (Realm realm : Realms) {
            System.out.println("rea:"+realm);
        }
        //登录类型对应的所有realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : Realms) {
            //根据登录类型和realm的名称进行匹配区分
            if (realm.getName().contains(userType)) {
                typeRealms.add(realm);
            }
        }

        //判断是单Realm还是多Realm，有多个Realm就会使用所有配置的Realm。只有一个的时候，就直接使用
        if (typeRealms.size() == 1) {
            logger.info("doSingleRealmAuthentication() execute");
            return doSingleRealmAuthentication(typeRealms.iterator().next(), token);
        } else {
            logger.info("doMultiRealmAuthentication() execute");
            return super.doMultiRealmAuthentication(typeRealms, token);
        }
    }
}