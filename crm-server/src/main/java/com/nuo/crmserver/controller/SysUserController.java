package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.service.SysUserService;
import com.nuo.crmserver.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<UserVO> login(@Validated @RequestBody LoginDTO dto) {
        return Result.success(sysUserService.login(dto));
    }
}
