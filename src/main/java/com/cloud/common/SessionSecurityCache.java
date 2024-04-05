package com.cloud.common;

import com.alibaba.fastjson2.JSON;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Data
@Slf4j
public class SessionSecurityCache implements SecurityCache{



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
    public void set(String key, UserInfo value) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tempKey = securityProperties.getUserInfoPrefixToCache() + value.getUserId();
        String tempValue = JSON.toJSONString(value);
        request.getSession().setAttribute(tempKey,tempValue);
        log.info("缓存key-->{},value:{}",tempKey,tempValue);
    }

    @Override
    public void removeUserInfo(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getSession().invalidate();
    }
}
