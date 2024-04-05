package com.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "easy.security")
@Data
public class SecurityProperties {

    /** 是否开启登录认证*/
    private Boolean enableLogin = true;

    /** 是否开启权限认证*/
    private Boolean enableAuth = true;

    /** token 在http请求头 key */
    private String tokenName = "token";

    /** 生成token 私匙*/
    private String tokenPrivateKey = "2cu$w%vo&ie*sc!";

    /** 缓存用户信息key的前缀*/
    private String userInfoPrefixToCache = "user-info-";

    /** 对其路径进行拦截 默认对所有请求进行拦截*/
    private String [] interceptPath = {"/**"};

    /** 放行需要登录请求*/
    private String [] ignorePaths = {};

    /** 未登录相应异常信息*/
    private String loginMsgError = "请先登录!";

    /** 无权限异常响应*/
    private String authMsgError = "您暂无权限!";

    /** 登录拦截器的优先级*/
    public int loginInterceptorOrder = 1;

    /** 权限切面优先级*/
    public int authAspectOrder = 1;

}
