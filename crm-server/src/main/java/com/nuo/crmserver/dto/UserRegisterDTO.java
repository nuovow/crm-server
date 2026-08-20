package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "登录名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,32}$", message = "登录名仅支持3-32位字母数字下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,64}$", message = "密码长度需为6-64位")
    private String password;

    private String confirmPassword;

    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式非法")
    private String phone;
}
