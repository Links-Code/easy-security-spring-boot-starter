package com.cloud.beans;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class UserInfo {

    /** 用户ID*/
    private Long userId;

    /** 用户名*/
    private String username;

    /** 密码*/
    private String password;

    /** 权限字符串*/
    private Set<String> authStr = new HashSet<>();

    /** 角色标识*/
    private List<Integer> rolesTag = new ArrayList<>();


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAuthStr() {
        return authStr;
    }

    public void setAuthStr(Set<String> authStr) {
        this.authStr = authStr;
    }

    public List<Integer> getRolesTag() {
        return rolesTag;
    }

    public void setRolesTag(List<Integer> rolesTag) {
        this.rolesTag = rolesTag;
    }
}
