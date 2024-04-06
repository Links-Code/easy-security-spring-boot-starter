package com.cloud.common;

import cn.hutool.crypto.SecureUtil;
import com.cloud.utils.MD5Util;

public class Md5PasswordHandle implements PasswordHandle{
    @Override
    public String encode(String password) {
        return MD5Util.encode(password);
    }

    @Override
    public boolean compare(String formPassword, String secretPassword) {
        return encode(formPassword).equals(secretPassword);
    }
}
