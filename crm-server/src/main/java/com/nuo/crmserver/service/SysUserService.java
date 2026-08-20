package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.entity.SysUser;
import com.nuo.crmserver.vo.UserVO;

public interface SysUserService extends IService<SysUser> {

    void register(UserRegisterDTO dto);

    UserVO login(LoginDTO dto);
}
