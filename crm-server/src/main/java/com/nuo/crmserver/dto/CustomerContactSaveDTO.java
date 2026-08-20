package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerContactSaveDTO {

    private Long id;

    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 32, message = "姓名不能超过32个字符")
    private String name;

    @Size(max = 32, message = "职务不能超过32个字符")
    private String position;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 64, message = "微信号不能超过64个字符")
    private String wechat;

    private Integer isPrimary;
}
