package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.RoleSaveDTO;
import com.nuo.crmserver.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    List<SysRole> listAll();

    void saveRole(RoleSaveDTO dto);

    void updateRole(RoleSaveDTO dto);

    void deleteRole(Long id);

    List<Long> getMenuIds(Long roleId);

    void assignMenus(Long roleId, List<Long> menuIds);
}
