package com.nuo.crmserver.service;

import com.nuo.crmserver.vo.MenuTreeVO;

import java.util.List;

public interface SysMenuService {

    /** 全量菜单树（角色配权界面用） */
    List<MenuTreeVO> tree();

    /** 当前登录用户的菜单树（前端导航栏用） */
    List<MenuTreeVO> userTree(Long userId);
}
