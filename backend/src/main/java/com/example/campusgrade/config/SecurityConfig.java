package com.example.campusgrade.config;

import com.example.campusgrade.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 的核心配置。
 *
 * <p>系统使用 JWT，因此不创建服务器 Session。登录接口允许匿名访问，
 * 其他接口必须先通过 {@link JwtAuthenticationFilter} 完成身份认证。</p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    /** 每次请求都会执行的 JWT 认证过滤器。 */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /** 通过构造器注入过滤器，便于 Spring 管理依赖。 */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 定义请求经过的安全过滤链。
     *
     * <p>由于 JWT 放在请求头中，不依赖 Cookie 会话，所以关闭 CSRF，
     * 并把会话策略设为 STATELESS（无状态）。</p>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 前后端分离项目不使用基于 Cookie 的登录会话。
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 登录前还没有 JWT，所以登录接口必须放行。
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated())
                // 在 Spring 默认用户名密码过滤器之前识别请求中的 JWT。
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /** 提供 BCrypt 密码编码器，用于密码加密和登录校验。 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** 配置允许访问后端的前端地址、HTTP 方法和请求头。 */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Vite 开发服务器可能通过 localhost 或 127.0.0.1 访问。
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        // 对系统中的所有 URL 应用以上跨域规则。
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
