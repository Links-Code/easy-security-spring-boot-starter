package com.cloud.common;

import com.cloud.beans.UserInfo;

public abstract class SecurityManage {

    final ThreadLocal<UserInfo> threadUserInfo = new ThreadLocal<>();

    /**
     * 获取用户信息
     * @return 用户信息
     */
    public abstract UserInfo getUserInfo();


    /**
     * 往thread local 保存用户信息
     * @param userInfo 用户信息
     */
    protected void save(UserInfo userInfo){
        threadUserInfo.set(userInfo);
    }

    /**
     * 移除用户信息
     */
    protected void remove(){
        threadUserInfo.remove();
    }

    /**
     * 验证密码是否正确
     * @param formPasswd 明文（表单传过来）
     * @param secretPasswd 密文 （数据库查询）
     * @return 是否密码正确
     */
    public abstract boolean verifyPassword(String formPasswd,String  secretPasswd);


    /**
     *保存用户信息 (登录成功后调用)
     * @param userInfo 用户信息
     * @return token
     */
    public abstract String load(UserInfo userInfo);

    /** 对于密码进行加密*/
    public abstract String encodePasswd(String passwd);

    /** 对密码进行解密*/
    public abstract String decodePasswd(String passwd);


}
