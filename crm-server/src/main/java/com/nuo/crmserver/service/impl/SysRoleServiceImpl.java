package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.RoleSaveDTO;
import com.nuo.crmserver.entity.SysRole;
import com.nuo.crmserver.entity.SysRoleMenu;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.SysRoleMapper;
import com.nuo.crmserver.mapper.SysRoleMenuMapper;
import com.nuo.crmserver.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nuo.crmserver.entity.SysUserRole;
import com.nuo.crmserver.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysRole> listAll() {
        return lambdaQuery().orderByAsc(SysRole::getId).list();
    }

    @Override
    public void saveRole(RoleSaveDTO dto) {
        checkCodeUnique(dto.getCode(), null);
        save(BeanUtil.copyProperties(dto, SysRole.class));
    }

    @Override
    public void updateRole(RoleSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("角色ID不能为空");
        }
        if (dto.getId() == 1L) {
            throw new BizException("内置管理员角色不允许修改");
        }
        checkCodeUnique(dto.getCode(), dto.getId());
        if (!updateById(BeanUtil.copyProperties(dto, SysRole.class))) {
            throw new BizException("角色不存在");
        }
    }

    @Override
    public void deleteRole(Long id) {
        if (id == 1L) {
            throw new BizException("内置管理员角色不允许删除");
        }
        Long bound = sysUserRoleMapper.selectCount(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
        if (bound > 0) {
            throw new BizException("该角色已分配给用户，请先解除分配");
        }
        removeById(id);
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
    }

    @Override
    public List<Long> getMenuIds(Long roleId) {
        return sysRoleMenuMapper.selectList(
                        new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(SysRoleMenu::getMenuId).toList();
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        if (getById(roleId) == null) {
            throw new BizException("角色不存在");
        }
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        if (menuIds != null && !menuIds.isEmpty()) {
            menuIds.forEach(menuId ->
                    sysRoleMenuMapper.insert(new SysRoleMenu(null, roleId, menuId, null)));
        }
    }

    private void checkCodeUnique(String code, Long excludeId) {
        SysRole existing = lambdaQuery().eq(SysRole::getCode, code).one();
        if (existing != null && !existing.getId().equals(excludeId)) {
            throw new BizException("角色编码已存在");
        }
    }
}
