package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DictSaveDTO {

    private Long id;

    @NotBlank(message = "字典类型不能为空")
    @Pattern(regexp = "^[a-z][a-z_0-9]{1,63}$", message = "字典类型为小写字母开头，可含下划线数字")
    private String type;

    @NotBlank(message = "显示文本不能为空")
    @Size(max = 64, message = "显示文本不能超过64字")
    private String label;

    @NotBlank(message = "存储值不能为空")
    @Size(max = 64, message = "存储值不能超过64字")
    private String value;

    private Integer sort;

    @Size(max = 255, message = "备注不能超过255字")
    private String remark;
}
