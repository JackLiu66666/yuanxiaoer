package com.yuanxiaoer.yuanxiaoer.config;

import com.yuanxiaoer.yuanxiaoer.agency_login.interceptor.AgencyLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类（注册拦截器）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AgencyLoginInterceptor())
                // 需要拦截的接口（代理商首页）
                .addPathPatterns("/agency/index/**")
                // 放行的接口（登录、注册）
                .excludePathPatterns("/agency/login", "/agency/register");
    }
}