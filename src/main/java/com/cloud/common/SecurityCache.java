package com.cloud.common;

import com.cloud.beans.UserInfo;

public interface SecurityCache {


    /**
     * 获取用户信息
     * @param key 键
     * @return 用户信息
     */
    UserInfo get(String key) throws Exception;


    /**
     * 设置用户信息
     * @param key 键
     * @param value 用户信息
     * @param overTime 过期时间
     */
    void set(String key, UserInfo value,Integer overTime) throws RuntimeException;

    /**
     * 移除用户信息
     * @param key 键
     */
    void removeUserInfo(String key);
}
