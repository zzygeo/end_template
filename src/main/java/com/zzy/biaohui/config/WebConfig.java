package com.zzy.biaohui.config;

import com.zzy.biaohui.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author zzy
 * @Description mvc config
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/logout", "/user/register")
                .excludePathPatterns("/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/static/**",
                        "/doc.html", "/favicon.ico", "/webjars/**",
                        "/v2/api-docs", "/v3/api-docs")
                .excludePathPatterns("/error");  // 排除错误页面的请求
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:/var/www/html/uploads/");
    }
}
