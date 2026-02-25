package com.yuanxiaoer.yuanxiaoer.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 Session 获取登录的管理员
        Object loginAdmin = request.getSession().getAttribute("loginAdmin");
        if (loginAdmin == null) {
            // 未登录，返回 401 JSON 提示
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"Admin 请先登录\",\"data\":null}");
            return false;
        }
        // 已登录，放行
        return true;
    }
}
