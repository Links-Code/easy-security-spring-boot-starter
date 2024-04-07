package com.cloud.common;


import com.cloud.utils.AESUtil;

public class AESPasswordHandle implements PasswordHandle{

    @Override
    public String encode(String password) {
        return AESUtil.encrypt(password);
    }

    @Override
    public String decode(String secretStr) {
        return AESUtil.decrypt(secretStr);
    }

    @Override
    public boolean compare(String formPassword, String secretPassword) {
        return decode(secretPassword).equals(formPassword);
    }
}
