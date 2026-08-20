package com.nuo.crmserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignMenusDTO {

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /** 允许传空数组 = 清空该角色全部权限 */
    private List<Long> menuIds;
}
