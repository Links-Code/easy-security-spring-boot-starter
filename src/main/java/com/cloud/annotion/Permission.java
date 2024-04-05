package com.cloud.annotion;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    String[] value() default {};

}
