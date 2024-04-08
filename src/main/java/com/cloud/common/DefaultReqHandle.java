package com.cloud.common;

import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import com.cloud.exceptions.UserInfoOverTimeException;
import com.cloud.exceptions.UNLoginException;
import com.cloud.utils.JWTUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

public class DefaultReqHandle implements ReqHandle {

    private static final Logger log = LoggerFactory.getLogger(DefaultReqHandle.class);


    private SecurityProperties securityProperties;

    private SecurityManage securityManage;

    private SecurityCache securityCache;


    public DefaultReqHandle(SecurityProperties securityProperties, SecurityManage securityManage,SecurityCache securityCache) {
        this.securityProperties = securityProperties;
        this.securityManage = securityManage;
        this.securityCache = securityCache;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(securityProperties.getTokenName());
        if (!StringUtils.hasText(token)){
            return getTokenToURL(request);
        }
        return  token;
    }

    @Override
    public void verify(String token) {
        //JWTUtil.verify(token, securityProperties.getTokenPrivateKey().getBytes());
        boolean isValid = JWTUtils.validateJWT(token);
        if (!isValid)
            throw new RuntimeException("非法Token");
    }

    /**
     * 解析token
     * @param token token
     * @return 解析后用户信息
     */
    @Override
    public UserInfo parse(String token) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> map = JWTUtils.parseJWT(token);
            String jsonMap = map.get("payload");
            Map readValue = objectMapper.readValue(jsonMap, Map.class);
            String JSON = (String) readValue.get(securityProperties.getUserInfoPrefixToCache());
            return objectMapper.readValue(JSON,UserInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfo cacheGetUserInfo(Long userId) {

        return ReqHandle.super.cacheGetUserInfo(userId);
    }


    /**
     * 拦截器是否放行请求
     * @param request 请求
     * @return 是否放行
     * @throws RuntimeException 异常
     */
    @Override
    public boolean reqThrough(HttpServletRequest request) throws Exception{
        //获取token
        String token = getToken(request);
        if (!StringUtils.hasText(token)){
            throw new UNLoginException(securityProperties.getLoginMsgError());
        }
        //验证是否本系统token
        verify(token);
        //获取用户信息
        UserInfo userInfo = parse(token);
        //从缓存中获取用户信息
        UserInfo info = securityCache.get(securityProperties.getUserInfoPrefixToCache() + userInfo.getUserId());
        if (info == null){
            log.error("😭:{} 用户登录过期",userInfo);
            throw new UserInfoOverTimeException("用户信息过期!");
        }
        //往threadLocal保存用户信息
        securityManage.threadUserInfo.set(info);
        return true;
    }


    /**
     * 从请求路径中获取token
     * @param request 请求
     * @return token
     */
    public String getTokenToURL(HttpServletRequest request){
        String token = request.getParameter(securityProperties.getTokenName());
        //尝试从 Query Param 获取token
        if (StringUtils.hasText(token)){
            return token;
        }
        return null;
    }


    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public SecurityManage getSecurityManage() {
        return securityManage;
    }

    public void setSecurityManage(SecurityManage securityManage) {
        this.securityManage = securityManage;
    }

    public SecurityCache getSecurityCache() {
        return securityCache;
    }

    public void setSecurityCache(SecurityCache securityCache) {
        this.securityCache = securityCache;
    }
}
