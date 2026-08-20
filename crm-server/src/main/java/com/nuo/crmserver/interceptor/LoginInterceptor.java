package com.nuo.crmserver.interceptor;

import com.nuo.crmserver.common.UserContext;
import com.nuo.crmserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 登录拦截器：验票(校验JWT)→登记(ThreadLocal存身份)→放行
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
            writeUnauthorized(response, "未登录");
            return false;
        }
        String token = auth.substring(7);
        try {
            Claims claims = jwtUtil.parseToken(token);
            UserContext.set(Long.valueOf(claims.getSubject()), claims.get("username", String.class));
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 线程池复用线程，不清理会串号：下个请求读到上个用户的身份
        UserContext.clear();
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        // 手拼JSON而非ObjectMapper：Boot4的Jackson3 ObjectMapper API与拦截器异常分支组合更繁琐，此处仅三字段
        String body = "{\"code\":401,\"message\":\"" + message + "\",\"data\":null}";
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }
}
