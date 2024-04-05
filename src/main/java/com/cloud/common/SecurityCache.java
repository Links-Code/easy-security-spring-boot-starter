package com.cloud.common;

import com.cloud.beans.UserInfo;

public interface SecurityCache {


    UserInfo get(String key);


    void set(String key, UserInfo value);

    void removeUserInfo(String key);
}
