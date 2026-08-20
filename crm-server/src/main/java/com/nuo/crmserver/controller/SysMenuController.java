package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.common.UserContext;
import com.nuo.crmserver.service.SysMenuService;
import com.nuo.crmserver.vo.MenuTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 全量菜单树：角色配权界面的权限树（管理员视角，含所有按钮）
     */
    @GetMapping("/tree")
    public Result<List<MenuTreeVO>> tree() {
        return Result.success(sysMenuService.tree());
    }

    /**
     * 当前登录用户的菜单树：前端按权限渲染导航栏
     */
    @GetMapping("/userTree")
    public Result<List<MenuTreeVO>> userTree() {
        return Result.success(sysMenuService.userTree(UserContext.getUserId()));
    }
}
