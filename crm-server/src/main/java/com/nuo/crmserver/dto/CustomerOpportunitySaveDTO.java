package com.nuo.crmserver.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerOpportunitySaveDTO {

    private Long id;

    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    @NotBlank(message = "商机名称不能为空")
    @Size(max = 64, message = "商机名称不能超过64个字符")
    private String title;

    @DecimalMin(value = "0", message = "预计成交金额不能为负数")
    private BigDecimal amount;

    @NotBlank(message = "商机阶段不能为空")
    private String stage;

    private LocalDate expectedDate;

    @Size(max = 255, message = "备注不能超过255个字符")
    private String remark;
}
