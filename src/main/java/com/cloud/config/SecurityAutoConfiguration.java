package com.cloud.config;

import com.cloud.aop.PermissionAspect;
import com.cloud.common.*;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
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


    @ConditionalOnMissingBean(value = {PasswordHandle.class})
    @Bean(name = {"passwordHandle"})
    public PasswordHandle createPasswordEncode(){
        return new Md5PasswordHandle();
    }

    @ConditionalOnMissingBean(value = {SecurityCache.class})
    @Bean(name = {"securityCache"})
    public SecurityCache createCache(SecurityProperties securityProperties){
        return new SessionSecurityCache(securityProperties);
    }

    @Bean(name = {"securityManage"})
    @DependsOn(value = {"passwordHandle","securityCache"})
    public DefaultSecurityManage createSecurityManage(PasswordHandle passwordHandle,SecurityProperties securityProperties,SecurityCache securityCache){
        return new DefaultSecurityManage(passwordHandle,securityProperties,securityCache);
    }


    @Bean(name = {"reqHandle"})
    @ConditionalOnMissingBean(value = {ReqHandle.class})
    @DependsOn(value = {"securityManage","securityCache"})
    public ReqHandle createReqHandle(SecurityProperties securityProperties,SecurityManage securityManage,SecurityCache securityCache){
        return new DefaultReqHandle(securityProperties,securityManage,securityCache);
    }

    @Bean(name = {"permissionAspect"})
    @DependsOn("securityManage")
    public PermissionAspect permissionAspect(SecurityManage securityManage,SecurityProperties securityProperties){
        return new PermissionAspect(securityManage,securityProperties);
    }

    @Bean(name = {"easySecurityInterceptor"})
    @DependsOn(value = {"securityManage","reqHandle"})
    public EasySecurityInterceptor securityInterceptor(ReqHandle reqHandle,SecurityProperties securityProperties,SecurityManage securityManage){
        return new EasySecurityInterceptor(reqHandle,securityProperties,securityManage);
    }

    @Bean
    @DependsOn("easySecurityInterceptor")
    public EasySecurityWebConfig webConfig(SecurityProperties securityProperties,EasySecurityInterceptor securityInterceptor){
        return new EasySecurityWebConfig(securityProperties,securityInterceptor);
    }

}

