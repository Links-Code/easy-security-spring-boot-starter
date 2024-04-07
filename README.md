

# Easy-Security

# Overview
Easy-Security 基于SpringBoot 实现用户登录认证以及授权框架  旨在轻量配置简便等!


# Feature
- 支持角色/权限字符串认证
- 支持JWT生成Token
- 支持MD5 AES 密码加密
- 缓存支持Session (默认) Redis


# Version

| SpringBoot Version | Easy-Security  Version |
|--------------------|------------------------|
| 2.XX.XX            | 0.XX.XX - 2.XX.XX      |
| 3.XX.XX            | 3.XX.XX                |


# Quick Start

1. Maven 引入依赖
```pom
<dependency>
    <groupId>com.cloud</groupId>
    <artifactId>easy-security-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```
2. 配置文件放行登录请求
```yaml
easy:
   security:
   #放行的请求   
   ignore-paths: ['/login']
```

3. 注册接口对密码进行加密(默认MD5方式加密) 

```java
@RestController
public class TestController {
    //注入 SecurityManage管理者（所有操作围绕这个bean进行的）
    @Autowired
    SecurityManage securityManage;

    @PostMapping(value = "/register")
    public String register(LoginForm form) {
        /**
         * 这里为了简便不调用业务方法
         * 省略一些操作....
         */
        //等待保持实体对象POJO
        User user = new User();
        //TODO 对于密码进行加密 （必须）
        String encodedPasswd = securityManage.encodePasswd(user.getPassword());
        user.setPassword(encodedPasswd);
        //再往数据库进行保存
        return "success";
    }
}
```

4. 登录接口
```java
@RestController
public class TestController {
    
    @Autowired
    SecurityManage securityManage;
    
    @PostMapping(value = "/login")
    public String login(LoginForm form){

        /**
         * 数据库操作查询出来用户信息封装到
         * com.cloud.UserInfo 如果保存更多用户信息 继承UserInfo即可
         * TODO 注意 UserInfo的userId必须有值
         */
        UserInfo userInfo = new UserInfo();
        //TODO 验证密码（必须）
        boolean verified = securityManage.verifyPassword(form.getPasswd(), passwd);
        if (!verified){
            throw new RuntimeException("密码不正确!");
        }
        //封装权限信息 （基于权限字符串认证）
        List<String> list = user.getRoles().stream().map(Role::getAuthStr).collect(Collectors.toList());
        userInfo.getAuthStr().addAll(list);
        //TODO 保存用户信息 生成Token 返回给前端 (必须)
        return securityManage.load(userInfo);
    }
}
```


5. 要对于接口进行权限检查加上注解即可(所有的类都是com.cloud包下的)
```java

@RestController
public class TestController {
    @Autowired
    SecurityManage securityManage;

    @PostMapping(value = "/test")
    //加上 Permission权限注解即可
    @Permission(value = "admin-query")
    public UserInfo test(){

        return securityManage.getUserInfo();
    }
}
```

- 详细配置请看Wiki

# Reference

- <img src="./doc/images/gpt.png" alt="GPT" width="30" height="30"> ChatGPT 4
