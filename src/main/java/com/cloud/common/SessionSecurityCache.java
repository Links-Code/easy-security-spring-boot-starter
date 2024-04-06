package com.cloud.common;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        else{
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue((String) info,UserInfo.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void set(String key, UserInfo value,Integer overTime) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String tempValue = null;
        try {
            tempValue = objectMapper.writeValueAsString(value);
            request.getSession().setAttribute(key + value.getUserId(),tempValue);
            log.info("缓存key-->{},value:{}",key + value.getUserId(),tempValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserInfo(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.getSession().invalidate();
    }
}
