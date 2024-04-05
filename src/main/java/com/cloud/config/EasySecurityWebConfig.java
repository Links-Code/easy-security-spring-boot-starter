package com.cloud.config;

import com.cloud.common.EasySecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.stream.Collectors;
public class EasySecurityWebConfig implements WebMvcConfigurer {

    SecurityProperties securityProperties;

    private final EasySecurityInterceptor securityInterceptor;

    public EasySecurityWebConfig(SecurityProperties securityProperties, EasySecurityInterceptor securityInterceptor) {
        this.securityProperties = securityProperties;
        this.securityInterceptor = securityInterceptor;
    }

    /**
     * 添加拦截器
     * @param registry 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                //对其路径进行拦截
                .addPathPatterns(Arrays.stream(securityProperties.getInterceptPath()).collect(Collectors.toList()))
                //放行路径
                .excludePathPatterns(Arrays.stream(securityProperties.getIgnorePaths()).collect(Collectors.toList()));
    }
}
