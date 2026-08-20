package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.entity.SysUser;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.SysUserMapper;
import com.nuo.crmserver.service.SysUserService;
import com.nuo.crmserver.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public UserVO login(LoginDTO dto) {
        SysUser user = lambdaQuery().eq(SysUser::getUsername, dto.getUsername()).one();
        // 用户不存在与密码错误统一提示，避免暴露账号是否存在
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException("账号已被禁用");
        }
        return UserVO.of(user);
    }
}
