package com.nuo.crmserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginVO {

    private String token;
    private UserVO user;
    /**
     * 权限编码清单：仅供前端渲染按钮显隐，真正的校验在后端拦截器
     */
    private List<String> permissions;
}
