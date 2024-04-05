package com.cloud.common;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
@Slf4j
public class DefaultSecurityManage extends SecurityManage{



    PasswordHandle passwordHandle;

    SecurityProperties securityProperties;

    SecurityCache securityCache;

    public DefaultSecurityManage(PasswordHandle passwordHandle, SecurityProperties securityProperties, SecurityCache securityCache) {
        this.passwordHandle = passwordHandle;
        this.securityProperties = securityProperties;
        this.securityCache = securityCache;
    }

    @Override
    public UserInfo getUserInfo() {
        return super.threadUserInfo.get();
    }

    /**
     * 验证密码是否正确
     * @param formPasswd 明文密码(表单密码)
     * @param secretPasswd 密文密码(数据库密码)
     * @return 是否密码正确
     */
    @Override
    public boolean verifyPassword(String formPasswd, String secretPasswd) {
        return passwordHandle.compare(formPasswd,secretPasswd);
    }


    /**
     * 登录后 存储用户信息
     * @param userInfo 用户信息
     * @return token
     */
    @Override
    public String load(UserInfo userInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(securityProperties.getUserInfoPrefixToCache(), JSON.toJSONString(userInfo));
        //往缓冲中存取
        securityCache.set(securityProperties.getTokenPrivateKey(),userInfo);
        //生成token
        String token = JWTUtil.createToken(map, securityProperties.getTokenPrivateKey().getBytes());
        log.info("用户:{} login success ! \uD83C\uDF89  生成token:{}",userInfo.getUsername(),token);
        return token;
    }

    /**
     * 对密码进行加密
     * @param passwd 明文密码
     * @return 加密后密码
     */
    @Override
    public String encodePasswd(String passwd) {
        return passwordHandle.encode(passwd);
    }

    /**
     * 对密码进行解密
     * @param passwd 密文密码
     * @return 解密后结果
     */
    @Override
    public String decodePasswd(String passwd) {
        return passwordHandle.decode(passwd);
    }
}
