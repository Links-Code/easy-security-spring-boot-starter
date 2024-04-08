package com.cloud;
import com.cloud.common.*;
import com.cloud.config.EasySecurityWebConfig;
import com.cloud.config.SecurityProperties;
import com.cloud.utils.JWTUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;
@Configuration
@EnableConfigurationProperties(value = {SecurityProperties.class})
@ConditionalOnWebApplication(
        type = ConditionalOnWebApplication.Type.SERVLET
)
@ConditionalOnClass({DispatcherServlet.class})
public class SecurityAutoConfiguration {


    /** 密码实现  默认MD5算法加密*/
    @ConditionalOnMissingBean(value = {PasswordHandle.class})
    @Bean(name = {"passwordHandle"})
    public PasswordHandle createPasswordEncode(){
        return new Md5PasswordHandle();
    }

    /** 缓存实现 默认session*/
    @ConditionalOnMissingBean(value = {SecurityCache.class})
    @Bean(name = {"securityCache"})
    public SecurityCache createCache(SecurityProperties securityProperties){
        return new SessionSecurityCache(securityProperties);
    }

    /**w权限管理者*/
    @Bean(name = {"securityManage"})
    @ConditionalOnMissingBean(value = {SecurityManage.class})
    @DependsOn(value = {"passwordHandle","securityCache"})
    public SecurityManage createSecurityManage(PasswordHandle passwordHandle,SecurityProperties securityProperties,SecurityCache securityCache){
        return new DefaultSecurityManage(passwordHandle,securityProperties,securityCache);
    }


    /** 登录拦截器执行逻辑*/
    @Bean(name = {"reqHandle"})
    @ConditionalOnMissingBean(value = {ReqHandle.class})
    @DependsOn(value = {"securityManage","securityCache"})
    public ReqHandle createReqHandle(SecurityProperties securityProperties,SecurityManage securityManage,SecurityCache securityCache){
        return new DefaultReqHandle(securityProperties,securityManage,securityCache);
    }

    /** 权限执行逻辑*/
    @Bean(name = {"authHandle"})
    @ConditionalOnMissingBean(value = {AuthHandle.class})
    @DependsOn("securityManage")
    public AuthHandle createAuthHandle(SecurityManage securityManage, SecurityProperties securityProperties){
        return new DefaultAuthHandle(securityManage,securityProperties);
    }


    /** 登录拦截器*/
    @Bean(name = {"easySecurityInterceptor"})
    @DependsOn(value = {"securityManage","reqHandle"})
    public EasySecurityInterceptor securityInterceptor(ReqHandle reqHandle,SecurityProperties securityProperties,SecurityManage securityManage){
        return new EasySecurityInterceptor(reqHandle,securityProperties,securityManage);
    }


    /** 权限拦截器*/
    @Bean(name = {"authSecurityInterceptor"})
    @DependsOn(value = {"authHandle","securityManage"})
    public AuthSecurityInterceptor AuthSecurityInterceptor(AuthHandle authHandle, SecurityProperties securityProperties,SecurityManage securityManage){
        return new AuthSecurityInterceptor(authHandle,securityProperties,securityManage);
    }

    /** 拦截器配置类*/
    @Bean
    @DependsOn({"easySecurityInterceptor","authSecurityInterceptor"})
    public EasySecurityWebConfig webConfig(SecurityProperties securityProperties, EasySecurityInterceptor securityInterceptor,AuthSecurityInterceptor authSecurityInterceptor){
        return new EasySecurityWebConfig(securityProperties,securityInterceptor,authSecurityInterceptor);
    }

    @Bean(name = "jWTUtils")
    public JWTUtils createJWTUtils(SecurityProperties securityProperties){
        return new JWTUtils(securityProperties);
    }

}

