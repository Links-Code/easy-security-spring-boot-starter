package com.cloud.common;
import com.cloud.annotion.Permission;
public interface AuthHandle {

    /**
     * 权限执行逻辑
     * @param permission 权限注解
     */
    public void process(Permission permission) throws Exception;
}
