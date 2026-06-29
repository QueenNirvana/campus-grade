package com.example.campusgrade.security;

import com.example.campusgrade.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT 令牌工具，负责签发令牌和读取令牌中的用户名。
 *
 * <p>JWT 的签名可防止客户端篡改用户身份；过期时间用于限制令牌有效期。</p>
 */
@Component
public class JwtTokenProvider {
    /** 由配置中的密钥字符串生成的 HMAC 签名密钥。 */
    private final SecretKey secretKey;
    /** 令牌有效小时数。 */
    private final long expirationHours;

    /** 从 application.yml 注入 JWT 密钥和有效期。 */
    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret,
                            @Value("${app.jwt.expiration-hours}") long expirationHours) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationHours = expirationHours;
    }

    /**
     * 为登录成功的用户生成 JWT。
     *
     * <p>subject 保存用户名，uid 和 role 是自定义声明，issuedAt 与 expiration
     * 分别记录签发时间和过期时间。</p>
     */
    public String createToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.username)
                .claim("uid", user.id)
                .claim("role", user.role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationHours, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 验证 JWT 签名并读取用户名。
     *
     * <p>令牌无效或过期时，JWT 库会抛出异常，由认证过滤器统一处理。</p>
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
