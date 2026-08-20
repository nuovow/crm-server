package com.nuo.crmserver.interceptor;

import com.nuo.crmserver.common.RequirePermission;
import com.nuo.crmserver.common.UserContext;
import com.nuo.crmserver.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 权限拦截器：读方法上的@RequirePermission声明，比对当前用户权限清单
 * 注册顺序在LoginInterceptor之后——先确认"你是谁"，再判断"你能不能"
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final SysUserService sysUserService;

    public PermissionInterceptor(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // handler是"即将被调用的东西"。是Controller方法时才是HandlerMethod，
        // 静态资源等场景是ResourceHttpRequestHandler——那种没有注解，直接放行
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 反射三连：拿方法对象→取方法上的注解实例→读注解属性
        // getMethodAnnotation找不到返回null，说明此接口没声明权限要求（如查询类接口），放行
        RequirePermission annotation = handlerMethod.getMethodAnnotation(RequirePermission.class);
        if (annotation == null) {
            return true;
        }
        String requiredPerm = annotation.value();

        List<String> permissions = sysUserService.getPermissions(UserContext.getUserId());
        if (permissions.contains(requiredPerm)) {
            return true;
        }
        writeForbidden(response);
        return false;
    }

    private void writeForbidden(HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        String body = "{\"code\":403,\"message\":\"无操作权限\",\"data\":null}";
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }
}
