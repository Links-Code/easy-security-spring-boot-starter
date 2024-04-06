package com.cloud.common;

import com.cloud.beans.UserInfo;
import javax.servlet.http.HttpServletRequest;

public interface ReqHandle {

    String getToken(HttpServletRequest request);

    /** 验证token*/
    void verify(String token);

    /** 解析token*/
    UserInfo parse(String token);

    /**
     * 缓存中获取用户信息
     */
    default UserInfo cacheGetUserInfo(Long userId) {
        return null;
    }


    /**
     * 是否对于登录请求进行放行
     * @param request 请求
     * @return 是否放行
     */
    default boolean reqThrough(HttpServletRequest request) throws RuntimeException{
        return true;
    }

}
