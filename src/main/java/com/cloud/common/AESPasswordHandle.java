package com.cloud.common;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import javax.crypto.SecretKey;
@Data
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
        return new String(aes.encrypt(password,CharsetUtil.CHARSET_UTF_8));
    }

    @Override
    public String decode(String secretStr) {
        return aes.decryptStr(secretStr, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public boolean compare(String formPassword, String secretPassword) {
        return decode(secretPassword).equals(formPassword);
    }


}
