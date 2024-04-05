package com.cloud.common;

import com.cloud.config.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class EasySecurityInterceptor implements HandlerInterceptor, Ordered {

    ReqHandle reqHandle;

    SecurityProperties securityProperties;

    SecurityManage securityManage;

    public EasySecurityInterceptor(ReqHandle reqHandle, SecurityProperties securityProperties, SecurityManage securityManage) {
        this.reqHandle = reqHandle;
        this.securityProperties = securityProperties;
        this.securityManage = securityManage;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //是否进行登录认证
        if (securityProperties.getEnableLogin()) {
            try {
                //需要拦截请求进行处理
                reqHandle.reqThrough(request);
            }catch (Exception e){
                //防止内存溢出
                securityManage.remove();
                log.error("拦截器发生异常:",e);
            }
            return true;
        }else {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //防止内存溢出
        securityManage.remove();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public int getOrder() {
        return securityProperties.getLoginInterceptorOrder();
    }

    public void setOrder(int order){
        securityProperties.setLoginInterceptorOrder(order);
    }
}
