package com.cloud.common;

import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import com.cloud.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DefaultSecurityManage extends SecurityManage{

    private static final Logger log = LoggerFactory.getLogger(DefaultSecurityManage.class);

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
        //往缓冲中存取
        securityCache.set(securityProperties.getUserInfoPrefixToCache(),userInfo,securityProperties.getOverTime());
        //生成token
        String token = JWTUtils.createJWT(userInfo);
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
