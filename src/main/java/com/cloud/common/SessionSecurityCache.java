package com.cloud.common;

import com.alibaba.fastjson2.JSON;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

public class SessionSecurityCache implements SecurityCache{

    private static final Logger log = LoggerFactory.getLogger(SessionSecurityCache.class);

    SecurityProperties securityProperties;

    public SessionSecurityCache(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public UserInfo get(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object info = request.getSession().getAttribute(key);
        if (info == null)
            return null;
        else
            return JSON.parseObject((String)info ,UserInfo.class);
    }

    @Override
    public void set(String key, UserInfo value,Integer overTime) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tempValue = JSON.toJSONString(value);
        request.getSession().setAttribute(key + value.getUserId(),tempValue);
        log.info("缓存key-->{},value:{}",key + value.getUserId(),tempValue);
    }

    @Override
    public void removeUserInfo(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getSession().invalidate();
    }
}
