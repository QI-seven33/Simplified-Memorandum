package com.media.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;  // 新增导入
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥（建议至少32个字符）
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 将字符串密钥转换为安全的 Key 对象
        // 如果密钥长度不足32字节，这里会抛出 WeakKeyException
        // 解决方案：确保 secretKey 长度 >= 32 字符
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 使用新版本 API（0.11.x 推荐写法）
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)  // 使用 Key 对象
                .setExpiration(exp)
                .compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥（必须与生成时使用的密钥一致）
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 使用新版本 API（0.11.x 推荐写法）
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}