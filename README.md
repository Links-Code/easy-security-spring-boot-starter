

# Easy-Security <img src="./doc/images/easy-security.png" alt="" width="40" height="40"> 

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
| 2.XX.XX            | 1.4.0 - 2.X.X          |
| 3.XX.XX            | 3.4.0 - 3.X.X          |


# Quick Start

## 1. Maven 引入依赖
```pom
<dependency>
    <groupId>io.github.links-code</groupId>
    <artifactId>easy-security-spring-boot-starter</artifactId>
    <version>1.4.0</version>
</dependency>
```

Gradle引入
```gredle
implementation group: 'io.github.links-code', name: 'easy-security-spring-boot-starter', version: '1.4.0'
```

## 2. 配置文件放行登录请求
```yaml
easy:
   security:
   #放行的请求   
   ignore-paths: ['/login']
```

## 3. 注册接口对密码进行加密(默认MD5方式加密) 

### - 注册密码进行加密存储

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

## 4. 登录接口

### - 验证密码

### -将数据库查询出来用户信息封装到UserInfo再通过框架的load保存用户信息

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


## 5. 要对于接口进行权限检查加上注解即可(所有的类都是com.cloud包下的)

### -加上对应权限注解即可

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

- 详细配置请往下见Doc

# Reference

- <img src="./doc/images/gpt.png" alt="GPT" width="30" height="30"> ChatGPT 4



# Doc 
## - Database Design
关于权限设计可以分两类


1.基于权限字符串授权
- 用户表设计 

| user_id | username | password                         | email       | create_time |
|---------|----------|----------------------------------|-------------|-------------|
| 1       | 张三       | 202cb962ac59075b964b07152d234b70 | 123@163.com | 2024-03-12  |
| 2       | 李四       | 202cb962ac59075b964b07152d234b70 | 159@qq.com  | 2024-04-01  |


- 角色表设计

| role_id | role_name | auth_str                              | create     |
|---------|-----------|---------------------------------------|------------|
| 1       | 管理员       | admin-query,admin-delete,admin-insert | 2024-03-01 |
| 2       | 普通用户      | people-query                          | 2024-01-01 |

- 角色与用户关系表

| role_user_id | role_id | user_id |
|--------------|---------|---------|
| 1            | 1       | 1       |
| 2            | 2       | 1       |


用户1 即使管理员也是普通用户

数据库查询（逗号进行拆分）出来吧字符串设置到 UserInfo的authStr中
再需要授权的接口上加上
例如 查询接口（只有管理员才可以进行查询 ）

```java
@Permission(value = "admin-query")
```


2.基于角色授权

- 两张表同上

- 角色表设计

| role_id | role_name | role_tag | create     |
|---------|-----------|----------|------------|
| 1       | 管理员       | 1        | 2024-03-01 |
| 2       | 普通用户      | 2        | 2024-01-01 |

数据库查询（逗号进行拆分）出来吧字符串设置到 UserInfo的rolesTag中
再需要授权的接口上加上
例如 查询接口（只有管理员才可以进行查询 ）

```java
@Permission(roles = {RoleType.ADMIN},type = AuthEnum.ROLE)
```

- 可以定义角色类型 (不要使用枚举)
```java
public class RoleType {
    public static final int ADMIN = 1;

    public static final int PERSON = 2;
}
```

## - 任意通过登录拦截器接口获取用户信息
```java

@Autowired
public SecurityManage securityManage;

//获取用户信息
UserInfo userInfo = securityManage.getUserInfo();

```

## - 自定义Redis缓存用户信息
1.引入redis依赖
```pom
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

2.配置文件设置redis
```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
```

3. 注入配置bean
```java
@Configuration
public class Config {
    
    @Bean(name = {"securityCache"})
    RedisSecurityCache securityCache(RedisTemplate<String,String> redisTemplate,SecurityProperties securityProperties){
        return new RedisSecurityCache(redisTemplate,securityProperties);
    }
}
```


## - 权限配置文件详解
```yaml
easy:
  security:
   # 是否开启登录认证 
   enableLogin: true
   # 是否开启权限认证  
   enableAuth: true
   # token在请求头中名字
   tokenName: 'token'
   # token 签名
   tokenPrivateKey: 'awd#awd?&8*'
   # 缓存用户信息前缀 默认 user-info- + 用户ID
   userInfoPrefixToCache: 'user-info-'
   # 放行请求(放行登录请求 以及 主页以/home开头的请求)     
   ignore-paths: ['/login','/home/**']
   # token过期时间以及缓存时间  针对redis缓存生效 默认session 自行设置session失效即可     
   overTime: 7
   # 提前几天进行用户信息的续期
   perExtendTime: 3
   # 登录拦截器优先级 值越大优先级越小
   loginInterceptorOrder: 1
   # 权限拦截器优先级
   authAspectOrder: 2
```

## - 替换AES进行密码加密
```java
@Bean(name = {"passwordHandle"})
AESPasswordHandle aesPasswordHandle(){
    return new AESPasswordHandle();
}
```

## 自定义一些逻辑如何注入bean 参考自动装配类即可  SecurityAutoConfiguration.java

