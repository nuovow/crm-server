package com.nuo.crmserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuo.crmserver.common.Perm;
import com.nuo.crmserver.common.RequirePermission;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.common.UserContext;
import com.nuo.crmserver.dto.AssignRolesDTO;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserPageQuery;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.service.SysUserService;
import com.nuo.crmserver.vo.LoginVO;
import com.nuo.crmserver.vo.SysUserVO;
import com.nuo.crmserver.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody UserRegisterDTO dto) {
        sysUserService.register(dto);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO dto) {
        return Result.success(sysUserService.login(dto));
    }

    /**
     * 查看当前登录人：验证拦截器+ThreadContext全链路
     */
    @GetMapping("/me")
    public Result<Map<String, Object>> me() {
        Map<String, Object> info = new HashMap<>();
        info.put("userId", UserContext.getUserId());
        info.put("username", UserContext.getUsername());
        info.put("permissions", sysUserService.getPermissions(UserContext.getUserId()));
        return Result.success(info);
    }

    /**
     * 用户分页列表：查询类接口只要求登录，不挂按钮权限
     */
    @GetMapping("/page")
    public Result<Page<SysUserVO>> page(UserPageQuery query) {
        return Result.success(sysUserService.pageByQuery(query));
    }

    /**
     * 启用/禁用账号
     */
    @RequirePermission(Perm.USER_EDIT)
    @PutMapping("/{id}/status/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 删除用户（逻辑删），同时物理删除其角色关联
     */
    @RequirePermission(Perm.USER_DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return Result.success();
    }

    /**
     * 查用户已绑定的角色ID列表：配权界面回显勾选状态
     */
    @RequirePermission(Perm.USER_ASSIGN)
    @GetMapping("/{id}/roles")
    public Result<List<Long>> getRoleIds(@PathVariable Long id) {
        return Result.success(sysUserService.getRoleIds(id));
    }

    /**
     * 分配角色：全量覆盖语义，传空数组即清空该用户全部角色
     */
    @RequirePermission(Perm.USER_ASSIGN)
    @PostMapping("/assignRoles")
    public Result<Void> assignRoles(@Validated @RequestBody AssignRolesDTO dto) {
        sysUserService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return Result.success();
    }
}
