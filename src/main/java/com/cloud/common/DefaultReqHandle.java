package com.cloud.common;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import com.cloud.exceptions.UnLoginException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Data
@Slf4j
public class DefaultReqHandle implements ReqHandle {

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
        JWTUtil.verify(token, securityProperties.getTokenPrivateKey().getBytes());
    }

    /**
     * 解析token
     * @param token token
     * @return 解析后用户信息
     */
    @Override
    public UserInfo parse(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        Object payload = jwt.getPayload(securityProperties.getUserInfoPrefixToCache());
        return JSON.parseObject(payload.toString().getBytes(),UserInfo.class);
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
    public boolean reqThrough(HttpServletRequest request) throws RuntimeException{
        //获取token
        String token = getToken(request);
        if (!StringUtils.hasText(token)){
            throw new RuntimeException("token不能为空");
        }
        //验证是否本系统token
        verify(token);
        //获取用户信息
        UserInfo userInfo = parse(token);
        //从缓存中获取用户信息
        UserInfo info = securityCache.get(securityProperties.getUserInfoPrefixToCache() + userInfo.getUserId());
        if (info == null){
            log.error("😭:{}",securityProperties.getLoginMsgError());
            throw new UnLoginException(securityProperties.getLoginMsgError());
        }
        //往threadLocal保存用户信息
        securityManage.threadUserInfo.set(info);
        return ReqHandle.super.reqThrough(request);
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
        //尝试从路径参数获取token
        String requestURL = request.getRequestURI();
        String[] parts = requestURL.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return null;
    }
}
