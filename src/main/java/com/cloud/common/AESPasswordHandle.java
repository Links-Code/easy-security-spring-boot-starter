package com.cloud.common;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.beans.factory.InitializingBean;
import javax.crypto.SecretKey;

public class AESPasswordHandle implements PasswordHandle, InitializingBean {


    private SecretKey key = null;

    private AES aes = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = SecureUtil.generateKey("woc?$oc^8(sc!");
        this.aes = SecureUtil.aes(key.getEncoded());
    }

    @Override
    public String encode(String password) {
        return new String(aes.encrypt(password));
    }

    @Override
    public String decode(String secretStr) {
        return aes.decryptStr(secretStr);
    }

    @Override
    public boolean compare(String formPassword, String secretPassword) {
        return decode(secretPassword).equals(formPassword);
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public AES getAes() {
        return aes;
    }

    public void setAes(AES aes) {
        this.aes = aes;
    }
}
