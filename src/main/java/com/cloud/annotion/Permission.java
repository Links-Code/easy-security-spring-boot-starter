package com.cloud.annotion;
import com.cloud.enums.AuthEnum;
import java.lang.annotation.*;
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    /** 权限字符串数组*/
    String[] value() default {};

    /** 角色类型*/
    int[] roles() default  {};

    /** 权限类型  默认基于权限字符串进行人证*/
    AuthEnum type() default AuthEnum.AUTH_STR;

}
