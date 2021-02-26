package com.bjf.config.shiro;

import com.bjf.pojo.BjfUser;
import com.bjf.service.impl.BjfUserServiceImpl;
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

public class UserRealm extends AuthorizingRealm {

    @Resource
    private BjfUserServiceImpl userService;


    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("user开始授权");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        //把在认证中放入的资源取出来
        BjfUser currentUser = (BjfUser) subject.getPrincipal();//拿到User对象

        //设置用户权限
        info.addStringPermission(currentUser.getPerms());
        //System.out.println(currentUser);
        return info;
    }

    /**
     * 认证
     * @param
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //System.out.println("User开始认证");
        //将token进行转换然后对比
        ShiroUsernamePasswordToken userToken = (ShiroUsernamePasswordToken) authenticationToken;
        //用户名认证
        //用户名，密码 数据库中取
        System.out.println(userToken.getUsername());
        BjfUser user = userService.queryBjfUserByName(userToken.getUsername());
        System.out.println(user);
        if(user == null){//没有这个人
            return null; //UnknownAccountException
        }

        //如果用户登录就在session中存值 没有就不存 用于前端页面判断
        Subject currentSubject = SecurityUtils.getSubject();
//        Session session = currentSubject.getSession();
//        session.setAttribute("loginUser",user);

        String name = user.getUUsername();
        //密码认证shiro自动处理 加密了 参数：用户资源 用户密码
        return new SimpleAuthenticationInfo(user,user.getUPwd(),getName());
    }
}
