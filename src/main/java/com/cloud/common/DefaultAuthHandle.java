package com.cloud.common;

import com.cloud.annotion.Permission;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import com.cloud.exceptions.UNLoginException;
import com.cloud.exceptions.UNPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

public class DefaultAuthHandle implements AuthHandle {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthHandle.class);

    public SecurityManage securityManage;

    public SecurityProperties securityProperties;

    public DefaultAuthHandle(SecurityManage securityManage, SecurityProperties securityProperties) {
        this.securityManage = securityManage;
        this.securityProperties = securityProperties;
    }

    /**
     * 执行切面权限逻辑
     * @param permission 权限注解
     * @throws Exception 异常
     */
    @Override
    public void process(Permission permission) throws Exception{
        //获取用户信息
        UserInfo userInfo = securityManage.getUserInfo();
        if (userInfo == null){
            throw new UNLoginException(securityProperties.getLoginMsgError());
        }
        switch (permission.type()){
            case AUTH_STR:
                //获取标注的注解值
                String[] values = permission.value();
                //判断是否具有权限
                boolean isAuth = Arrays.stream(values).anyMatch(userInfo.getAuthStr()::contains);
                if (!isAuth){
                    log.error("\uD83D\uDE1E 用户: {} -- :{}",userInfo,securityProperties.getAuthMsgError());
                    throw new UNPermissionException(securityProperties.getAuthMsgError());
                }
                break;
            case ROLE:
                //获取标注的注解值
                int[] roles = permission.roles();
                List<Integer> rolesTag = userInfo.getRolesTag();
                boolean match = Arrays.stream(roles).anyMatch(rolesTag::contains);
                if (!match){
                    log.error("\uD83D\uDE1E 用户: {} -- :{}",userInfo,securityProperties.getAuthMsgError());
                    throw new UNPermissionException(securityProperties.getAuthMsgError());
                }
                break;
            default:
                log.error("未匹配到认证类型");
        }
    }
}
