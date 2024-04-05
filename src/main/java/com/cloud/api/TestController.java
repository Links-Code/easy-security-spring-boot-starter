package com.cloud.api;

import com.cloud.annotion.Permission;
import com.cloud.beans.UserInfo;
import com.cloud.common.DefaultSecurityManage;
import com.cloud.common.ReqHandle;
import com.cloud.common.SecurityManage;
import com.cloud.config.SecurityProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;

@RestController
public class TestController implements ApplicationContextAware {



    public SecurityManage securityManage;

    public ApplicationContext applicationContext;

    public SecurityProperties securityProperties;

    @Autowired
    public ReqHandle reqHandle;

    @Autowired
    public TestController(SecurityManage securityManage, SecurityProperties securityProperties) {
        this.securityManage = securityManage;
        this.securityProperties = securityProperties;
    }

    @PostMapping("/login")
    private String login(UserInfo userInfo){
        UserInfo info = new UserInfo();
        info.setPassword("202cb962ac59075b964b07152d234b70");
        info.setUserId(userInfo.getUserId());
        info.setUsername(userInfo.getUsername());

        HashSet<String> objects = new HashSet<>();
        objects.add("setUsername");
        info.setAuthStr(objects);
        System.out.println(securityManage);
        System.out.println(applicationContext);
        boolean verified = securityManage.verifyPassword(userInfo.getPassword(), info.getPassword());
        System.out.println("验证密码后结果: " + verified);
        throw new RuntimeException("123");
        //return securityManage.load(info);
    }


    @PostMapping("/test")
    public UserInfo test(){
        return securityManage.getUserInfo();
    }


    @PostMapping("/auth")
    @Permission()
    public UserInfo auth(){
        return securityManage.getUserInfo();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
