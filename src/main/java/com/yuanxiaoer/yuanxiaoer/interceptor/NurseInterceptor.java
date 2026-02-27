package com.yuanxiaoer.yuanxiaoer.interceptor;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 护士登录拦截器
 */
@Component
public class NurseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取Session
        HttpSession session = request.getSession();
        // 判断是否登录（Session中是否有loginNurse）
        Object loginNurse = session.getAttribute("loginNurse");
        if (loginNurse == null) {
            // 未登录，返回错误信息
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("请先登录")));
            return false; // 拦截请求
        }
        // 已登录，放行
        return true;
    }
}