package com.neusoft.elm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域资源共享 (CORS) 配置
 * 允许前端 Vue 应用访问后端 API
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  // 允许 /api/** 路径跨域
                        .allowedOriginPatterns("*")  // 允许所有来源（生产环境应限制为具体域名）
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的 HTTP 方法
                        .allowedHeaders("*")  // 允许所有请求头
                        .allowCredentials(true)  // 允许携带认证信息（如 Cookie）
                        .maxAge(3600);  // 预检请求缓存时间（秒）
            }
        };
    }
}
