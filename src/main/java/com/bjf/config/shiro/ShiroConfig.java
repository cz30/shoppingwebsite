package com.bjf.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {
    //3、ShiroFilterFactoryBean过滤对象
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(securityManager);

        //添加shiro的内置过滤器
        /**
         * anon：无需认证就可以访问
         * authc：必须认证了才能访问
         * user：必须拥有 记住我 功能才能用
         * perms：拥有对某个资源的权限才能访问
         * role：拥有某个角色权限才能访问
         */
        //因为setFilterChainDefinitionMap参数是map类型
        //因为是链式的所以用linkedHashMap
        //拦截
        Map<String,String> filterMap = new LinkedHashMap<>();

        //自定义拦截器限制并发人数,参考博客
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //限制同一帐号同时在线的个数
        filtersMap.put("kickout", kickoutSessionControlFilter());
        bean.setFilters(filtersMap);

        filterMap.put("/user/namelogin","anon");
        filterMap.put("/user/phoneLogin","anon");
        filterMap.put("/merchant/nameLogin","anon");
        filterMap.put("/merchant/phoneLogin","anon");
        filterMap.put("/sendCode","anon");
        filterMap.put("/register/**","anon");
        filterMap.put("/homePage/**","anon");

        filterMap.put("/show/trade","user");
        filterMap.put("/add/new/address","user");
        filterMap.put("/change/address","user");
        filterMap.put("/default/address","user");
        filterMap.put("/create/order","user");
        filterMap.put("/order/**","user");

        //filterMap.put("/**","authc");


        //授权 正常的情况下没有授权会跳转到未授权页面
        /*filterMap.put("/index","kickout,anon");
        filterMap.put("/","kickout,anon");
        filterMap.put("/user/login","kickout,anon");
        filterMap.put("/add","user");
        filterMap.put("/ulogin","kickout,anon");*/

        /*filterMap.put("/user/add","user");
        filterMap.put("/user/update","user");
        filterMap.put("/user/update","busy");
        filterMap.put("/user/toregister","anon");
        filterMap.put("/userLogin","anon");*/

        //登录拦截
        /*filterMap.put("/user/add","authc");
        filterMap.put("/user/update","authc");*/
        //也可以使用通配符
        //filterMap.put("/user/*","authc");
        //filterMap.put("/MD5","anon");
        /*filterMap.put("/**","authc");*/



        //设置登录的请求 如果没有权限 进行跳转
        bean.setLoginUrl("/Login.html");
        //这里的/index是后台的接口名,非页面,登录成功后要跳转的链接
        //bean.setSuccessUrl("/index.html");
        //未授权页面
        //bean.setUnauthorizedUrl("/noauth");
        //其他资源都需要认证  authc 表示需要认证才能进行访问 user表示配置记住我或认证通过可以访问的地址
        //filterMap.put("/**", "user");
        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }

/*    //2、DafaultwebSecurityManager安全对象
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManger(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);


        return securityManager;
    }*/



    //核心
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm());
        realms.add(busyRealm());
        securityManager.setAuthorizer(modularRealmAuthorizer());
        securityManager.setAuthenticator(modularRealmAuthenticator());//需要在realm之前
        securityManager.setRealms(realms);

        //配置记住我
        securityManager.setRememberMeManager(cookieRememberMeManager());

        //配置ehcache缓存管理器
        securityManager.setCacheManager(ehCacheManager());

        //配置自定义session管理，使用ehcache 或redis
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    /**
     * 并发控制
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        kickoutSessionControlFilter.setCacheManager(ehCacheManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/index");
        return kickoutSessionControlFilter;
    }



    @Bean
    public ModularRealmAuthorizer modularRealmAuthorizer(){
        MyModularRealmAuthorizer modularRealmAuthorizer = new MyModularRealmAuthorizer();
        return modularRealmAuthorizer;
    }

    /**
     * 系统自带的realm管理，主要针对多realm认证
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        //重写自己的ModularRealmAuthenticator
        MyModularRealmAuthenticator modularRealmAuthenticator = new MyModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }




    /**
     * cookie管理对象;记住我功能,rememberMe管理器
     *
     * @return org.apache.shiro.web.mgt.CookieRememberMeManager
     * @author zhuyongsheng
     * @date 2019/8/15
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    /**
     * FormAuthenticationFilter 过滤器 过滤记住我
     * @return
     */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

    /**
     *   * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe
     *      *
     *      * @return org.apache.shiro.web.servlet.SimpleCookie
     *      * @author zhuyongsheng
     *      * @date 2019/8/15
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        /*
         * 设为true后，只能通过http访问，javascript无法访问
         * 防止xss读取cookie
         */
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }


 /*   *
     * shiro缓存管理器
     *
     * @return com.ccb.web.shiro.RedisCacheManager
     * @author zhuyongsheng
     * @date 2019/8/15

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        //redis中针对不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("username");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(60 * 1000 * 60);
        return redisCacheManager;
    }*/

    /**
     * 配置session监听
     */
    @Bean
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }

    /**
     * 配置会话ID生成器
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO(){
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //使用ehCacheManager
        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        //设置session缓存的名字 默认为 shiro-activeSessionCache
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return enterpriseCacheSessionDAO;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置会话管理器，设定会话超时及保存
     */
    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(ehCacheManager());

        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(1800000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(3600000);

        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }


    /**
     * redis缓存
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 连接redis
     * @return
     */
    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("192.168.0.200");
        redisManager.setPort(6379);
        redisManager.setPassword("");
        return redisManager;
    }


    /**
     * shiro缓存管理器
     * 需要添加到securityManager中
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:shiro/ehcache-shiro.xml");
        return cacheManager;
    }










    //1、创建realm对象  需要自定义类
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        userRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        userRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息默认为false
        userRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称 在ehcache-shiro.xml中有对应的缓存的配置
        userRealm.setAuthorizationCacheName("authorizationCache");

        return userRealm;
    }

    @Bean
    public BusyRealm busyRealm(){
        return new BusyRealm();
    }

    //整合ShiroDialect：用来整合shiro thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    /**
     * 配置shiro redisManager
     */
}
