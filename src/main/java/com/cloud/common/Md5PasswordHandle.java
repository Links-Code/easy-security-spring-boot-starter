package com.cloud.common;

import cn.hutool.crypto.SecureUtil;

public class Md5PasswordHandle implements PasswordHandle{
    @Override
    public String encode(String password) {
        return SecureUtil.md5(password);
    }

    @Override
    public boolean compare(String formPassword, String secretPassword) {
        return encode(formPassword).equals(secretPassword);
    }
}
