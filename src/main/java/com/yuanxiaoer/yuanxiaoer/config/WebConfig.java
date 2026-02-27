package com.yuanxiaoer.yuanxiaoer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.yuanxiaoer.yuanxiaoer.interceptor.AgencyInterceptor;
import com.yuanxiaoer.yuanxiaoer.interceptor.InsuranceCompanyInterceptor;
import com.yuanxiaoer.yuanxiaoer.interceptor.AdminInterceptor;
import com.yuanxiaoer.yuanxiaoer.interceptor.NurseInterceptor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Web配置类（注册拦截器）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 代理商拦截器
        registry.addInterceptor(new AgencyInterceptor())
                // 需要拦截的接口（代理商首页）
                .addPathPatterns("/agency/**", "/agencyAuditApplication/**")
                // 放行的接口（登录、注册）
                .excludePathPatterns("/agency/login", "/agency/register");
        registry.addInterceptor(new InsuranceCompanyInterceptor())
                // 需要拦截的接口
                .addPathPatterns("/insurance_company/**")
                // 放行的接口
                .excludePathPatterns("/insurance_company/login", "/insurance_company/register");
        registry.addInterceptor(new AdminInterceptor())
                // 需要拦截的接口
                .addPathPatterns("/admin/**")
                // 放行的接口
                .excludePathPatterns("/admin/login");
        // 护士拦截器
        registry.addInterceptor(new NurseInterceptor())
                // 需要拦截的接口
                .addPathPatterns("/nurse/**")
                // 放行的接口（登录、注册、检查审核状态）
                .excludePathPatterns("/nurse/login", "/nurse/register");
    }

    @Value("${upload.base-path}")
    private String uploadBasePath;
    // 配置静态资源访问，允许前端访问upload目录
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadBasePath);
    }
}