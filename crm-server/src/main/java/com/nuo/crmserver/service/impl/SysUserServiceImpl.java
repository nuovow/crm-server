package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.common.UserContext;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserPageQuery;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.entity.SysUser;
import com.nuo.crmserver.entity.SysUserRole;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.SysMenuMapper;
import com.nuo.crmserver.mapper.SysUserMapper;
import com.nuo.crmserver.mapper.SysUserRoleMapper;
import com.nuo.crmserver.service.SysUserService;
import com.nuo.crmserver.util.JwtUtil;
import com.nuo.crmserver.vo.LoginVO;
import com.nuo.crmserver.vo.SysUserVO;
import com.nuo.crmserver.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private final SysMenuMapper sysMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    public SysUserServiceImpl(JwtUtil jwtUtil, SysMenuMapper sysMenuMapper, SysUserRoleMapper sysUserRoleMapper) {
        this.jwtUtil = jwtUtil;
        this.sysMenuMapper = sysMenuMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Override
    public void register(UserRegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BizException("两次输入的密码不一致");
        }
        boolean exists = lambdaQuery().eq(SysUser::getUsername, dto.getUsername()).exists();
        if (exists) {
            throw new BizException("登录名已存在");
        }
        SysUser user = BeanUtil.copyProperties(dto, SysUser.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        SysUser user = lambdaQuery().eq(SysUser::getUsername, dto.getUsername()).one();
        // 用户不存在与密码错误统一提示，避免暴露账号是否存在
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException("账号已被禁用");
        }
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        return new LoginVO(token, UserVO.of(user), getPermissions(user.getId()));
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return sysMenuMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public Page<SysUserVO> pageByQuery(UserPageQuery query) {
        Page<SysUser> page = lambdaQuery()
                .like(StrUtil.isNotBlank(query.getUsername()), SysUser::getUsername, query.getUsername())
                .eq(query.getStatus() != null, SysUser::getStatus, query.getStatus())
                .orderByDesc(SysUser::getCreateTime)
                .page(new Page<>(query.getPageNum(), query.getPageSize()));
        Page<SysUserVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(SysUserVO::of).toList());
        return voPage;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (id.equals(UserContext.getUserId())) {
            throw new BizException("不能禁用当前登录账号");
        }
        if (id == 1L) {
            throw new BizException("内置管理员不允许禁用");
        }
        SysUser update = new SysUser();
        update.setId(id);
        update.setStatus(status);
        if (!updateById(update)) {
            throw new BizException("用户不存在");
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (id.equals(UserContext.getUserId())) {
            throw new BizException("不能删除当前登录账号");
        }
        if (id == 1L) {
            throw new BizException("内置管理员不允许删除");
        }
        if (!removeById(id)) {
            throw new BizException("用户不存在");
        }
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
    }

    @Override
    public List<Long> getRoleIds(Long userId) {
        if (getById(userId) == null) {
            throw new BizException("用户不存在");
        }
        return sysUserRoleMapper.selectList(
                        new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).toList();
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        if (getById(userId) == null) {
            throw new BizException("用户不存在");
        }
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(roleId ->
                    sysUserRoleMapper.insert(new SysUserRole(null, userId, roleId, null)));
        }
    }
}
