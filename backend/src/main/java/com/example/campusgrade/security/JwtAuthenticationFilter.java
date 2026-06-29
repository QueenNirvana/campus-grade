package com.example.campusgrade.security;

import com.example.campusgrade.entity.User;
import com.example.campusgrade.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 请求认证过滤器，每个 HTTP 请求只执行一次。
 *
 * <p>过滤器从 Authorization 请求头读取令牌，解析出用户名后查询最新用户状态，
 * 最后把认证信息放入 Spring Security 上下文，供权限注解和业务层使用。</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /** 负责 JWT 的生成和解析。 */
    private final JwtTokenProvider jwtTokenProvider;
    /** 根据令牌中的用户名查询数据库用户。 */
    private final UserMapper userMapper;

    /** 使用构造器注入过滤器依赖。 */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    /**
     * 尝试认证当前请求；没有令牌或令牌无效时不建立登录身份。
     *
     * @param request 当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param chain 后续过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        // 标准 JWT 请求头格式为：Authorization: Bearer <token>。
        if (header != null && header.startsWith("Bearer ")) {
            try {
                // substring(7) 去掉前面的 "Bearer "。
                String username = jwtTokenProvider.getUsername(header.substring(7));
                User user = userMapper.findByUsername(username);
                // 即使令牌有效，账号被删除或停用后也不能继续访问系统。
                if (user != null && user.status != null && user.status == 1) {
                    CurrentUser principal = new CurrentUser(user.id, user.username, user.realName, user.role);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.role)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ignored) {
                // 令牌过期、签名错误或格式错误时清空身份，后续安全链会拒绝受保护请求。
                SecurityContextHolder.clearContext();
            }
        }
        // 无论是否认证成功，都必须继续执行后面的过滤器。
        chain.doFilter(request, response);
    }
}
