package com.cloud.common;

import com.cloud.beans.UserInfo;

/**
 * 自定义 token保存用户信息
 */
@FunctionalInterface
public interface LoadFunc {

    public UserInfo saveToToken();
}
