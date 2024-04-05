package com.cloud.common;

public interface PasswordHandle {


    /**
     * 加密
     * @param password 明文密码
     * @return 加密后密码
     */
    String encode(String password);

    /***
     * 解密
     * @param secretStr 密文
     * @return 解密后结果
     */
    default String decode(String secretStr){
        return null;
    }

    /**
     * 对比密码是否正确
     * @param formPassword 明文 (用户输入)
     * @param secretPassword 密文 (数据库获取)
     * @return 是否正确
     */
    boolean compare(String formPassword,String secretPassword);
}
