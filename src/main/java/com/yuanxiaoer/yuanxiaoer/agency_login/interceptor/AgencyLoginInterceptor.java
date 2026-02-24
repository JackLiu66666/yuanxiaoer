package com.yuanxiaoer.yuanxiaoer.agency_login.interceptor;

import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AgencyLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从Session获取登录用户
        Agency loginAgency = (Agency) request.getSession().getAttribute("loginAgency");
        if (loginAgency == null) {
            // 未登录，返回JSON提示
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"test请先登录\",\"data\":null}");
            return false;
        }
        // 已登录，放行
        return true;
    }
}
