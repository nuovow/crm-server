package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuo.crmserver.dto.AssignRolesDTO;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserPageQuery;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.entity.SysUser;
import com.nuo.crmserver.vo.LoginVO;
import com.nuo.crmserver.vo.SysUserVO;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    void register(UserRegisterDTO dto);

    LoginVO login(LoginDTO dto);

    List<String> getPermissions(Long userId);

    Page<SysUserVO> pageByQuery(UserPageQuery query);

    void updateStatus(Long id, Integer status);

    void deleteUser(Long id);

    List<Long> getRoleIds(Long userId);

    void assignRoles(Long userId, List<Long> roleIds);
}
