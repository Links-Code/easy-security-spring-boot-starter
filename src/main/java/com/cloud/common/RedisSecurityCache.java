package com.cloud.common;
import com.alibaba.fastjson2.JSON;
import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;
public class RedisSecurityCache implements SecurityCache{

    public SecurityProperties securityProperties;

    public RedisTemplate<String,String> securityRedisTemplate;

    public RedisSecurityCache(RedisTemplate<String,String> redisTemplate,SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.securityRedisTemplate = redisTemplate;
    }

    @Override
    public UserInfo get(String key) {
        //检查是否需要进行延期
        extendTime(key);
        return JSON.parseObject((String) securityRedisTemplate.opsForValue().get(key),UserInfo.class);
    }

    @Override
    public void set(String key, UserInfo value,Integer overTime) {

        securityRedisTemplate.opsForValue().set(key + value.getUserId(),JSON.toJSONString(value),overTime.longValue(), TimeUnit.DAYS);

    }

    @Override
    public void removeUserInfo(String key) {
        securityRedisTemplate.delete(key);
    }


    /**
     * 延期过期时间
     * @param key 键
     */
    public void extendTime(String key){
        Long ttl = securityRedisTemplate.getExpire(key);
        // 如果当前键设置过期时间，或者距离过期时间小于3天，则进行延期
        if (ttl != null && ttl < TimeUnit.DAYS.toSeconds(securityProperties.getPerExtendTime())) {
            securityRedisTemplate.expire(key, securityProperties.getOverTime().longValue(), TimeUnit.DAYS);
        }
    }
}
