package com.nuo.crmserver.config;

import com.nuo.crmserver.interceptor.LoginInterceptor;
import com.nuo.crmserver.interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** 拦截器白名单：登录注册、错误页、接口文档 */
    private static final String[] WHITE_LIST = {
            "/user/login",
            "/user/register",
            "/error",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private final LoginInterceptor loginInterceptor;
    private final PermissionInterceptor permissionInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor, PermissionInterceptor permissionInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.permissionInterceptor = permissionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 顺序至关重要：先验票(登录)，再查权限
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(WHITE_LIST);
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(WHITE_LIST);
    }

    /**
     * 跨域：前后端分离开发必需。带凭证时不能用*，必须用allowedOriginPatterns
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
