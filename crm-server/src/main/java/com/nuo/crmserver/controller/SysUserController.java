package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.common.UserContext;
import com.nuo.crmserver.dto.LoginDTO;
import com.nuo.crmserver.dto.UserRegisterDTO;
import com.nuo.crmserver.service.SysUserService;
import com.nuo.crmserver.vo.LoginVO;
import com.nuo.crmserver.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO dto) {
        return Result.success(sysUserService.login(dto));
    }

    /**
     * 查看当前登录人：验证拦截器+ThreadContext全链路
     */
    @GetMapping("/me")
    public Result<Map<String, Object>> me() {
        Map<String, Object> info = new HashMap<>();
        info.put("userId", UserContext.getUserId());
        info.put("username", UserContext.getUsername());
        return Result.success(info);
    }
}
