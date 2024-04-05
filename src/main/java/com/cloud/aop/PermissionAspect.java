package com.cloud.aop;

import com.cloud.annotion.Permission;
import com.cloud.beans.UserInfo;
import com.cloud.common.SecurityManage;
import com.cloud.config.SecurityProperties;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;


import java.util.Arrays;
@Aspect
@Slf4j
@Order(2)
public class PermissionAspect {


    SecurityManage securityManage;


    SecurityProperties securityProperties;

    public PermissionAspect(SecurityManage securityManage, SecurityProperties securityProperties) {
        this.securityManage = securityManage;
        this.securityProperties = securityProperties;
    }

    @Pointcut("@annotation(com.cloud.annotion.Permission)")
    public void annotationPointcut() {}

//    @Before("annotationPointcut() && @annotation(permission)")
//    public void beforeAdvice(Permission permission) {
//        System.out.println("aop ---------------");
////        //获取用户信息
////        UserInfo userInfo = securityManage.getUserInfo();
////        //获取标注的注解值
////        String[] values = permission.value();
////        //判断是否具有权限
////        boolean isAuth = Arrays.stream(values).anyMatch(userInfo.getAuthStr()::contains);
////        if (!isAuth){
////            log.error("\uD83D\uDE1E :{}",securityProperties.getAuthMsgError());
////            throw new UnPermissionException(securityProperties.getAuthMsgError());
////        }
//    }

//    @Override
//    public int getOrder() {
//        return securityProperties.getAuthAspectOrder();
//    }
//
//    public void setOrder(int order){
//        securityProperties.setAuthAspectOrder(order);
//    }
}
