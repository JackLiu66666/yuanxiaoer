package com.yuanxiaoer.yuanxiaoer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
// import io.swagger.v3.oas.models.security.SecurityRequirement;
// import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j/Swagger 配置类
 * 访问地址：http://localhost:8081/api/v1/doc.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("院小二 API 文档")
                        .version("1.0.0")
                        .description("院小二平台接口文档")
                        .contact(new Contact()
                                .name("Yuanxiaoer")
                                .email("support@yuanxiaoer.com")));
        // 注意：当前项目使用 Session 认证，不需要配置 JWT 安全方案
        // 如果未来要改用 JWT 认证，可以取消以下注释：
        /*
        return openAPI
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Token，格式：Bearer {token}")));
        */
    }

    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 自定义 OpenAPI 配置
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    // 可以在这里对 tag 进行自定义处理
                });
            }
        };
    }
}