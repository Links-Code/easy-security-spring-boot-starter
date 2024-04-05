package com.cloud.beans;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class UserInfo {

    /** 用户ID*/
    private Long userId;

    /** 用户名*/
    private String username;

    /** 密码*/
    private String password;

    /** 权限字符串*/
    private Set<String> authStr = new HashSet<>();

}
