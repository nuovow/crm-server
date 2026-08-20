package com.nuo.crmserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.CustomerOpportunityQuery;
import com.nuo.crmserver.dto.CustomerOpportunitySaveDTO;
import com.nuo.crmserver.service.CustomerOpportunityService;
import com.nuo.crmserver.vo.CustomerOpportunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opportunity")
@RequiredArgsConstructor
public class CustomerOpportunityController {

    private final CustomerOpportunityService opportunityService;

    /**
     * 分页查询商机
     */
    @GetMapping("/page")
    public Result<Page<CustomerOpportunityVO>> page(CustomerOpportunityQuery query) {
        return Result.success(opportunityService.pageByQuery(query));
    }

    /**
     * 查询某客户的商机列表（客户详情页内嵌使用）
     */
    @GetMapping("/list/{customerId}")
    public Result<List<CustomerOpportunityVO>> listByCustomer(@PathVariable Long customerId) {
        return Result.success(opportunityService.listByCustomerId(customerId));
    }

    /**
     * 新增商机
     */
    @PostMapping
    public Result<Void> saveOpportunity(@Validated @RequestBody CustomerOpportunitySaveDTO dto) {
        opportunityService.saveOpportunity(dto);
        return Result.success();
    }

    /**
     * 修改商机（含阶段流转）
     */
    @PutMapping
    public Result<Void> updateOpportunity(@Validated @RequestBody CustomerOpportunitySaveDTO dto) {
        opportunityService.updateOpportunity(dto);
        return Result.success();
    }

    /**
     * 删除商机（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOpportunity(@PathVariable Long id) {
        opportunityService.removeById(id);
        return Result.success();
    }
}
