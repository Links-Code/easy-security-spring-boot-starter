package com.cloud.utils;

import com.cloud.beans.UserInfo;
import com.cloud.config.SecurityProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JWTUtils implements InitializingBean {

    private static String SECRET;
    private static final String ALGORITHM = "HmacSHA256";

    public static int OVER_TIME = 14;

    SecurityProperties securityProperties;

    public static String KEY_PRE = "user-info-";

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET = securityProperties.getTokenPrivateKey();
        OVER_TIME = securityProperties.getOverTime();
        KEY_PRE = securityProperties.getUserInfoPrefixToCache();
    }

    public JWTUtils(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public static String createJWT(UserInfo userInfo) {
        // 创建 Header
        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        String encodedHeader = base64Encode(header);

        // 创建 Payload
        Map<String, String> payload = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            payload.put(KEY_PRE,objectMapper.writeValueAsString(userInfo));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        long currentTimeMillis = System.currentTimeMillis();
        payload.put("exp", String.valueOf(currentTimeMillis + 3600L * 1000 * 24 * 14 * OVER_TIME)); // 设置过期时间为当前时间后的一小时
        String encodedPayload = base64Encode(payload);

        // 创建 Signature
        String signature = generateSignature(encodedHeader + "." + encodedPayload, SECRET);

        // 构建 JWT
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public static Map<String, String> parseJWT(String jwt) {
        String[] parts = jwt.split("\\.");
        Map<String, String> claims = new HashMap<>();
        claims.put("header", base64Decode(parts[0]));
        claims.put("payload", base64Decode(parts[1]));
        return claims;
    }

    public static boolean validateJWT(String jwt) {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            // JWT格式不正确，返回false
            return false;
        }
        String signature = generateSignature(parts[0] + "." + parts[1], SECRET);
        return signature != null && signature.equals(parts[2]);
    }

    private static String base64Encode(Map<String, String> data) {
        // 将 Map 转换为 JSON 字符串，然后进行 Base64 编码
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(data);
            return Base64.getEncoder().encodeToString(json.getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private static String base64Decode(String encodedString) {
        byte[] bytes = Base64.getDecoder().decode(encodedString);
        return new String(bytes);
    }

    private static String generateSignature(String data, String secret) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), ALGORITHM);
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
