package com.cloud.common;

import com.cloud.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class EasySecurityInterceptor implements HandlerInterceptor, Ordered {

    private static final Logger log = LoggerFactory.getLogger(EasySecurityInterceptor.class);

    ReqHandle reqHandle;

    SecurityProperties securityProperties;

    SecurityManage securityManage;

    private final ThreadLocal<String> logInterceptorTL = new ThreadLocal<>();

    public EasySecurityInterceptor(ReqHandle reqHandle, SecurityProperties securityProperties, SecurityManage securityManage) {
        this.reqHandle = reqHandle;
        this.securityProperties = securityProperties;
        this.securityManage = securityManage;
    }

    private static final String HANDLED_FLAG_ATTRIBUTE = "easySecurityInterceptorHandled";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 由于在控制器或者业务层抛出异常 导致preHandle回执行两次 所以进行判断
         */
        if (logInterceptorTL.get() != null){
            //防止内存泄漏
            securityManage.threadUserInfo.remove();
            logInterceptorTL.remove();
            return true;
        }
        //是否进行登录认证
        if (securityProperties.getEnableLogin()) {
            try {
                //设置执行标记
                logInterceptorTL.set("processed");
                //需要拦截请求进行处理
                return reqHandle.reqThrough(request);
            }catch (Exception e){
                //防止内存溢出
                securityManage.remove();
                log.error("😭登录拦截器发生异常");
                throw e;
            }
        }else {
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //防止内存溢出
        securityManage.remove();
        logInterceptorTL.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public int getOrder() {
        return securityProperties.getLoginInterceptorOrder();
    }

    public void setOrder(int order){
        securityProperties.setLoginInterceptorOrder(order);
    }
}
