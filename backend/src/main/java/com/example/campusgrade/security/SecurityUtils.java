package com.example.campusgrade.security;

import com.example.campusgrade.common.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 读取当前登录用户及判断角色的静态工具类。
 *
 * <p>工具类不需要创建对象，因此构造方法设为 private。</p>
 */
public final class SecurityUtils {
    /** 禁止外部实例化工具类。 */
    private SecurityUtils() {
    }

    /**
     * 从 Spring Security 上下文取得当前用户。
     *
     * @return 已通过 JWT 认证的用户信息
     * @throws BusinessException 请求没有有效登录身份时抛出
     */
    public static CurrentUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CurrentUser user)) {
            throw new BusinessException("未登录或登录已过期");
        }
        return user;
    }

    /** 判断当前用户是否为管理员。 */
    public static boolean isAdmin() {
        return "ADMIN".equals(currentUser().role);
    }

    /** 判断当前用户是否为教师。 */
    public static boolean isTeacher() {
        return "TEACHER".equals(currentUser().role);
    }

    /** 判断当前用户是否为学生。 */
    public static boolean isStudent() {
        return "STUDENT".equals(currentUser().role);
    }
}
