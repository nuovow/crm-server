package com.nuo.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nuo.crmserver.entity.SysMenu;
import com.nuo.crmserver.mapper.SysMenuMapper;
import com.nuo.crmserver.service.SysMenuService;
import com.nuo.crmserver.vo.MenuTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<MenuTreeVO> tree() {
        List<SysMenu> menus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSort));
        return buildTree(menus);
    }

    @Override
    public List<MenuTreeVO> userTree(Long userId) {
        return buildTree(sysMenuMapper.selectMenusByUserId(userId));
    }

    /**
     * 平铺列表组装成树：parentId=0为根，children挂到对应父节点
     */
    private List<MenuTreeVO> buildTree(List<SysMenu> menus) {
        Map<Long, List<MenuTreeVO>> byParent = menus.stream()
                .map(MenuTreeVO::of)
                .collect(Collectors.groupingBy(MenuTreeVO::getParentId));
        List<MenuTreeVO> roots = byParent.getOrDefault(0L, List.of());
        roots.forEach(root -> attachChildren(root, byParent));
        return roots;
    }

    private void attachChildren(MenuTreeVO node, Map<Long, List<MenuTreeVO>> byParent) {
        List<MenuTreeVO> children = byParent.getOrDefault(node.getId(), List.of());
        node.setChildren(children);
        children.forEach(child -> attachChildren(child, byParent));
    }
}
