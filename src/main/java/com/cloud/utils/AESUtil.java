package com.cloud.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {


    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);

    public static String KEY_STR = "";

    private static byte[] KEY_BYTE;

    static {
        // 生成AES密钥
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // 可以是128, 192或256位
            SecretKey secretKey = keyGenerator.generateKey();
            KEY_BYTE = secretKey.getEncoded();
            KEY_STR = Base64.getEncoder().encodeToString(KEY_BYTE);
            log.info("----AES生产秘钥--- {}",KEY_STR);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String encrypt(String strToEncrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY_BYTE, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY_BYTE, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


    public static byte[] getKeyByte() {
        return KEY_BYTE;
    }

    public static void setKeyByte(byte[] keyByte) {
        KEY_BYTE = keyByte;
    }
}
