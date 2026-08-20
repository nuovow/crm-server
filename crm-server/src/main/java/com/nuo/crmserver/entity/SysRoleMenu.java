package com.nuo.crmserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 角色-菜单关联：纯关系表，物理删除
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu {

    @TableId
    private Long id;

    private Long roleId;
    private Long menuId;
    private LocalDateTime createTime;
}
