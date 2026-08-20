package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerSaveDTO {

    private Long id;

    @NotBlank(message = "客户名称不能为空")
    @Size(max = 64, message = "客户名称不能超过64个字符")
    private String name;

    @Size(max = 32, message = "行业不能超过32个字符")
    private String industry;

    @Size(max = 32, message = "客户来源不能超过32个字符")
    private String source;

    @Pattern(regexp = "^[ABC]$", message = "客户级别只能是A、B、C")
    private String level;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 255, message = "备注不能超过255个字符")
    private String remark;
}
