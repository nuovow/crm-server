package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerFollowSaveDTO {

    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    private Long contactId;

    @NotBlank(message = "跟进方式不能为空")
    private String followType;

    @NotBlank(message = "跟进内容不能为空")
    @Size(max = 1000, message = "跟进内容不能超过1000个字符")
    private String content;

    private LocalDateTime nextTime;
}
