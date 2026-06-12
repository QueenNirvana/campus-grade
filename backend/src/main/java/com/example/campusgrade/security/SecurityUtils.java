package com.example.campusgrade.security;

import com.example.campusgrade.common.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static CurrentUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CurrentUser user)) {
            throw new BusinessException("未登录或登录已过期");
        }
        return user;
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(currentUser().role);
    }

    public static boolean isTeacher() {
        return "TEACHER".equals(currentUser().role);
    }

    public static boolean isStudent() {
        return "STUDENT".equals(currentUser().role);
    }
}
