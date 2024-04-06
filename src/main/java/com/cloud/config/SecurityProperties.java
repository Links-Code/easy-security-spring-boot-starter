package com.cloud.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "easy.security")
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

    /** 需要放行请求*/
    private String [] ignorePaths = {};

    /** 用户信息过期实现  默认14天后过期*/
    private Integer overTime = 14;

    /** 前几天进行用户信息刷新 (延期用户信息过期时间)*/
    private Integer perExtendTime = 3;

    /** 未登录相应异常信息*/
    private String loginMsgError = "请先登录!";

    /** 无权限异常响应*/
    private String authMsgError = "您暂无权限!";

    /** 登录拦截器的优先级*/
    private int loginInterceptorOrder = 1;

    /** 权限切面优先级 (比登录拦截器优先级低)*/
    private int authAspectOrder = 2;

    public Boolean getEnableLogin() {
        return enableLogin;
    }

    public void setEnableLogin(Boolean enableLogin) {
        this.enableLogin = enableLogin;
    }

    public Boolean getEnableAuth() {
        return enableAuth;
    }

    public void setEnableAuth(Boolean enableAuth) {
        this.enableAuth = enableAuth;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenPrivateKey() {
        return tokenPrivateKey;
    }

    public void setTokenPrivateKey(String tokenPrivateKey) {
        this.tokenPrivateKey = tokenPrivateKey;
    }

    public String getUserInfoPrefixToCache() {
        return userInfoPrefixToCache;
    }

    public void setUserInfoPrefixToCache(String userInfoPrefixToCache) {
        this.userInfoPrefixToCache = userInfoPrefixToCache;
    }

    public String[] getInterceptPath() {
        return interceptPath;
    }

    public void setInterceptPath(String[] interceptPath) {
        this.interceptPath = interceptPath;
    }

    public String[] getIgnorePaths() {
        return ignorePaths;
    }

    public void setIgnorePaths(String[] ignorePaths) {
        this.ignorePaths = ignorePaths;
    }

    public Integer getOverTime() {
        return overTime;
    }

    public void setOverTime(Integer overTime) {
        this.overTime = overTime;
    }

    public Integer getPerExtendTime() {
        return perExtendTime;
    }

    public void setPerExtendTime(Integer perExtendTime) {
        this.perExtendTime = perExtendTime;
    }

    public String getLoginMsgError() {
        return loginMsgError;
    }

    public void setLoginMsgError(String loginMsgError) {
        this.loginMsgError = loginMsgError;
    }

    public String getAuthMsgError() {
        return authMsgError;
    }

    public void setAuthMsgError(String authMsgError) {
        this.authMsgError = authMsgError;
    }

    public int getLoginInterceptorOrder() {
        return loginInterceptorOrder;
    }

    public void setLoginInterceptorOrder(int loginInterceptorOrder) {
        this.loginInterceptorOrder = loginInterceptorOrder;
    }

    public int getAuthAspectOrder() {
        return authAspectOrder;
    }

    public void setAuthAspectOrder(int authAspectOrder) {
        this.authAspectOrder = authAspectOrder;
    }
}
