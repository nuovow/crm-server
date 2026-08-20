package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.CustomerFollowSaveDTO;
import com.nuo.crmserver.service.CustomerFollowService;
import com.nuo.crmserver.vo.CustomerFollowVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class CustomerFollowController {

    private final CustomerFollowService followService;

    /**
     * 新增跟进记录（事件表：只插入不更新）
     */
    @PostMapping
    public Result<Void> addFollow(@Validated @RequestBody CustomerFollowSaveDTO dto) {
        followService.addFollow(dto);
        return Result.success();
    }

    /**
     * 客户跟进时间线（按时间倒序）
     */
    @GetMapping("/list/{customerId}")
    public Result<List<CustomerFollowVO>> listByCustomer(@PathVariable Long customerId) {
        return Result.success(followService.listByCustomerId(customerId));
    }
}
