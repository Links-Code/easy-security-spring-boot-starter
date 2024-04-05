package com.cloud;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EasySecuritySpringBootStarterApplicationTests {

    @Test
    void contextLoads() {
        byte[] data = "我是一段测试字符串".getBytes();
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
        //签名
        byte[] signed = sign.sign(data);
        //验证签名
        boolean verify = sign.verify(data, signed);


        String s = SecureUtil.md5("123");
        System.out.println(s);


    }

}
