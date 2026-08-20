package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Perm;
import com.nuo.crmserver.common.RequirePermission;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.AssignMenusDTO;
import com.nuo.crmserver.dto.RoleSaveDTO;
import com.nuo.crmserver.service.SysRoleService;
import com.nuo.crmserver.vo.SysRoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 全量角色列表：用户配权界面的角色勾选框也用它
     */
    @GetMapping("/list")
    public Result<List<SysRoleVO>> list() {
        return Result.success(sysRoleService.listAll().stream().map(SysRoleVO::of).toList());
    }

    /**
     * 新增角色
     */
    @RequirePermission(Perm.ROLE_ADD)
    @PostMapping
    public Result<Void> saveRole(@Validated @RequestBody RoleSaveDTO dto) {
        sysRoleService.saveRole(dto);
        return Result.success();
    }

    /**
     * 修改角色
     */
    @RequirePermission(Perm.ROLE_EDIT)
    @PutMapping
    public Result<Void> updateRole(@Validated @RequestBody RoleSaveDTO dto) {
        sysRoleService.updateRole(dto);
        return Result.success();
    }

    /**
     * 删除角色：有用户占用时拒绝，防悬挂引用
     */
    @RequirePermission(Perm.ROLE_DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        sysRoleService.deleteRole(id);
        return Result.success();
    }

    /**
     * 查角色已勾选的菜单ID列表：配权树回显
     */
    @RequirePermission(Perm.ROLE_ASSIGN)
    @GetMapping("/{id}/menus")
    public Result<List<Long>> getMenuIds(@PathVariable Long id) {
        return Result.success(sysRoleService.getMenuIds(id));
    }

    /**
     * 分配菜单权限：全量覆盖语义，事务内先删后插
     */
    @RequirePermission(Perm.ROLE_ASSIGN)
    @PostMapping("/assignMenus")
    public Result<Void> assignMenus(@Validated @RequestBody AssignMenusDTO dto) {
        sysRoleService.assignMenus(dto.getRoleId(), dto.getMenuIds());
        return Result.success();
    }
}
