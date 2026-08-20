package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignRolesDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 允许传空数组 = 清空该用户全部角色 */
    private List<Long> roleIds;
}
