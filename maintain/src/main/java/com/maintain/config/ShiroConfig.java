package com.maintain.config;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.maintain.filter.KickoutSessionFilter;
import com.maintain.shiro.SimpleAuthRealm;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置
 *
 * 出于开发效率的考虑，shiro的功能并没有全部使用，而是选择了部分，
 * 目前使用的shiro的功有
 * 1.登录
 * 2.角色验证
 * 3.权限码验证
 * 登录是直接使用的shiro的接口，在自己的controller中实现的，其主要目的
 * 就是为了用2和3的功能，但2和3必须基于1来实现，所以这样做。
 *         UsernamePasswordToken token = new UsernamePasswordToken(
 *                 username, password);
 *         Subject subject = SecurityUtils.getSubject();
 *         subject.login(token);
 * 2和3是很好用的功能，但目前只能在接口上使用，而无法在页面上，原因是
 * 我们的页面用的是纯html，没有用任何的后端模板
 * 为什么呢？
 * 这就是开发效率和功能的矛盾
 * 如果用了模板（Thymeleaf或Velocity），然后用ModelAndView，自然是可以实现权限的
 * 但是这意味着，每新增一个页面都需要新增一个方法，写一堆重复的代码，不值得。
 * 思来想去，最后的解决方案如下：
 * 需要权限验证的页面上方添加一行标识，
 *  {[( Company )]}
 *  Company就是Shiro里面的用户的Role，和后台接口用@RequiredRoles的取值是一致的，
 *  多种角色可以用下面的方式，用英文逗号分隔
 *  [( Company,Svip )]}
 *  如果不进行验证，则不加标识或者写空标识即可，如：{[(  )]}
 *  配合AuthFilter，读取标识，进行判断，也是不错的方法
 *  至此，还有最后一个功能没有解决，那就是在页面里面实现细粒度的权限控制，这个如果
 *  也自己实现的话，会稍稍难一点，但这不是最主要的问题，最主要的问题是如果在一个页面里面
 *  不同的角色都添加不同的判断，那么页面的可读性会很差，而且会容易因为粗心把某些功能漏给
 *  别的角色，鉴于此，推荐做法如下：
 *  除了完全通用的功能，如修改密码，其他的功能每个角色单独写一份，这样就可以明确每个
 *  角色的功能，而且在修改的时候，不会影响到别的角色，唯一的缺点可能是工作量会多那么
 *  一点点点，不过也无所谓，前后端上上下下那么多功能已经帮你节省了90%的时间，剩下的
 *  这点，花那么10%来弄一下吧。
 *
 *  最后还有一点，layAdmin这个框架其实只有一个主页面，那就是index.html，剩下的页面全是
 *  在这个页面上渲染出来的，如下：
 *  http://localhost:2500/index.html#/adminUser/list，我们称之为完整路径
 *  index.html是主页面
 *  adminUser/list.html是子页面，是在index.html上面渲染出来的,单独
 *  访问其实是没有意义的，我们称之为部分路径
 *  如果用户已经登录，那么访问完整路径，会首先渲染index.html，之后渲染
 *  子页面，若用户没有权限，则子页面的返回是没有权限的页面的html内容，直接
 *  展示即可
 *  但如果用户没有登录，直接访问完整路径就会有问题了，这个情况下，访问完整路径
 *  之后，子页面，子页面可以知道用户没有登录，但是，它没法让客户端跳转到登录页面，
 *  因为子页面是通过ajax请求的，即使重定向，转发，甚至把页面替换成登录页面都没用，
 *  layAdmin本身是提供了一种实现的，它在每次请求子页面之前，都会进行权限验证，就是
 *  发送一个请求，根据响应决定做一些事情，所以在UserCenterController下面才会有
 *  一个/auth的页面，它本来除了登录还是用来做页面的权限验证的，不过现在有了更好的方法
 *  就只用它做登录的判断了
 *  至此，web端的权限基本上已经解决了，兼顾了开发效率与安全，真棒o(￣▽￣)ｄ good
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * </br>1,配置shiro安全管理器接口securityManage;
     * </br>2,shiro 连接约束配置filterChainDefinitions;
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 配置shiro安全管理器 SecurityManager
        bean.setSecurityManager(securityManager);
        //添加kickout认证
        HashMap<String,Filter> hashMap = new HashMap<String,Filter>();
        hashMap.put("kickout",kickoutSessionFilter());
        bean.setFilters(hashMap);
        //filterChainDefinitions拦截器map必须用：LinkedHashMap，因为它必须保证有序
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 要放行的页面
        filterMap.put("/login", "anon");  //登录页面
        filterMap.put("/doLogin", "anon"); //登录请求
        filterMap.put("/register", "anon"); //注册请求
        filterMap.put("/initUser", "anon"); //初始化用户
        filterMap.put("/auth", "anon");  //权限验证

        // 配置不会被拦截的链接 从上向下顺序判断
        filterMap.put("/images/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/json/**", "anon");
        filterMap.put("/layui/**", "anon");
        filterMap.put("/lib/**", "anon");
        filterMap.put("/style/**", "anon");
        filterMap.put("/views/**", "anon");
        filterMap.put("/index.html", "anon");  //layAdmin主页面
        filterMap.put("/index.js", "anon");  //layAdmin主js入口
        filterMap.put("/config.js", "anon");  //layAdmin配置

        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
        filterMap.put("/**", "authc");

        // 添加 shiro 过滤器
        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }

    /**
     * shiro安全管理器设置realm认证和ehcache缓存管理
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        // 关联realm
        manager.setRealm(shiroRealm());
        //注入ehcache缓存管理器;
        manager.setCacheManager(ehCacheManager());
        //注入session管理器;
        manager.setSessionManager(sessionManager());
        //注入Cookie记住我管理器
        manager.setRememberMeManager(rememberMeManager());

        return manager;
    }

    /**
     * 3.创建身份认证 Realm
     */
    @Bean
    public SimpleAuthRealm shiroRealm() {
        SimpleAuthRealm realm = new SimpleAuthRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }


    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于 // md5(md5(""));
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    /**
     * RememberMe 的功能
     */
    // 创建 Cookie
    @Bean
    public SimpleCookie remeberMeCookie() {
        logger.info("记住我，设置cookie过期时间！");
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // //记住我cookie生效时间30天 ,单位秒  [10天]
        cookie.setMaxAge(864000);
        // 设置只读模型
        //cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * 配置cookie记住我管理器
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        logger.debug("配置cookie记住我管理器！");
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        return cookieRememberMeManager;
    }


    /**
     *
     * 功能描述: 同一个用户多设备登录限制
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/23 10:22
     */
    public KickoutSessionFilter kickoutSessionFilter(){
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionFilter.setCacheManager(ehCacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionFilter;
    }


    /**
     * ehcache缓存管理器；shiro整合ehcache：
     * 通过安全管理器：securityManager
     * 单例的cache防止热部署重启失败
     * @return EhCacheManager
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        logger.debug("shiro整合ehcache缓存：ShiroConfiguration.getEhCacheManager()");
        EhCacheManager ehcache = new EhCacheManager();
        CacheManager cacheManager = CacheManager.getCacheManager("shiro");
        if(cacheManager == null){
            try {
                cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:config/ehcache.xml"));
            } catch (CacheException | IOException e) {
                e.printStackTrace();
            }
        }
        ehcache.setCacheManager(cacheManager);
        return ehcache;
    }

    /**
     *
     * 功能描述: sessionManager添加session缓存操作DAO
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/23 10:31
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

		sessionManager.setSessionDAO(enterCacheSessionDAO());
		sessionManager.setSessionIdCookie(sessionIdCookie());
		return sessionManager;
	}

    /**
     * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     */
    @Bean
    public EnterpriseCacheSessionDAO enterCacheSessionDAO() {
        EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return enterCacheSessionDAO;
    }


    /**
     *
     * 功能描述: 自定义cookie中session名称等配置
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/23 10:34
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        //DefaultSecurityManager
        SimpleCookie simpleCookie = new SimpleCookie();
        //sessionManager.setCacheManager(ehCacheManager());
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("SHRIOSESSIONID");
        //单位秒
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * *
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 整合 Shiro 标签
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
