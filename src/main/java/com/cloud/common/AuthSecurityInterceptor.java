package com.cloud.common;

import com.cloud.annotion.Permission;
import com.cloud.config.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


public class AuthSecurityInterceptor implements HandlerInterceptor , Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthSecurityInterceptor.class);

    public AuthHandle aspectHandle;

    public SecurityProperties securityProperties;

    public AuthSecurityInterceptor(AuthHandle aspectHandle, SecurityProperties securityProperties) {
        this.aspectHandle = aspectHandle;
        this.securityProperties = securityProperties;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(Permission.class)) {
                Permission annotation = handlerMethod.getMethodAnnotation(Permission.class);
                //执行切面逻辑
                aspectHandle.process(annotation);
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return securityProperties.getAuthAspectOrder();
    }

    public void setOrder(int order){
        securityProperties.setAuthAspectOrder(order);
    }
}
