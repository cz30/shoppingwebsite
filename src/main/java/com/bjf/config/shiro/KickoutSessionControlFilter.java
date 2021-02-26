package com.bjf.config.shiro;

import com.bjf.pojo.BjfMerchant;
import com.bjf.pojo.BjfUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControlFilter extends AccessControlFilter {

    //踢出后到的地址
    private String kickoutUrl;

    //踢出之前登陆的/之后登陆的用户 默认踢出之前登录的用户
    private boolean kickoutAfter = false;

    //同一个账号最大的会话数 默认1
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }

    //是否允许访问，返回true表示允许
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    //表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示已经了(比如重定向到另一个页面)
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request,response);
        if(!subject.isAuthenticated() && !subject.isRemembered()){
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        String username = null;
        if(subject.getPrincipal().toString().contains("BjfUser")){
             username = ((BjfUser)subject.getPrincipal()).getUUsername();
        }else if(subject.getPrincipal().toString().contains("BjfMerchant")){
             username = ((BjfMerchant)subject.getPrincipal()).getMcUsername();
        }
        Serializable sessionId = session.getId();
        //初始化用户的队列放到缓存里
        Deque<Serializable> deque = cache.get(username);
        if(deque == null){
            deque = new LinkedList<>();
            cache.put(username,deque);
        }
        //如果队列里没有此sessionId，且用户没有被踢出，放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null){
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(username,deque);
        }
        //如果队列里的session数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if (kickoutAfter) { //如果踢出后者
                kickoutSessionId=deque.getFirst();
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //如果被踢出了，直接退出，重定向到踢出后的地址
        if(session.getAttribute("kickout") != null){
            //会话被踢出
            try{
                subject.logout();
            }catch (Exception e){

            }
            //          throw new CommonException("101", "您已被迫下线，若不是您本人操作，请立即修改密码！");
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(101);
            res.sendRedirect(kickoutUrl);
            //          RoleAuthorizationFilter.sendFastJson(response, "您已被迫下线，若不是您本人操作，请立即修改密码！");
//          WebUtils.issueRedirect(request, response, kickoutUrl);
            return false;
        }
        return true;
    }

}
