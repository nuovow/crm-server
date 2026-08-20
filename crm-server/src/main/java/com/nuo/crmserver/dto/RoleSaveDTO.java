package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleSaveDTO {

    private Long id;

    @NotBlank(message = "角色名不能为空")
    @Size(max = 32, message = "角色名不能超过32字")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    @Pattern(regexp = "^[A-Z][A-Z_0-9]{1,31}$", message = "角色编码为大写字母开头，可含下划线数字")
    private String code;

    @Size(max = 255, message = "备注不能超过255字")
    private String remark;
}
