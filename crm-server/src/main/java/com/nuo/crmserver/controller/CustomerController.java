package com.nuo.crmserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.CustomerQuery;
import com.nuo.crmserver.dto.CustomerSaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.service.CustomerService;
import com.nuo.crmserver.vo.CustomerVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 分页查询客户
     */
    @GetMapping("/page")
    public Result<Page<CustomerVO>> page(CustomerQuery query) {
        Page<Customer> page = customerService.pageByQuery(query);
        Page<CustomerVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(CustomerVO::of).toList());
        return Result.success(voPage);
    }

    /**
     * 根据ID查询单个客户
     */
    @GetMapping("/{id}")
    public Result<CustomerVO> getCustomer(@PathVariable Long id) {
        return Result.success(CustomerVO.of(customerService.getCustomerById(id)));
    }

    /**
     * 新增客户
     */
    @PostMapping
    public Result<Void> saveCustomer(@Validated @RequestBody CustomerSaveDTO dto) {
        customerService.saveCustomer(dto);
        return Result.success();
    }

    /**
     * 根据ID修改客户
     */
    @PutMapping
    public Result<Void> updateCustomer(@Validated @RequestBody CustomerSaveDTO dto) {
        customerService.updateCustomer(dto);
        return Result.success();
    }

    /**
     * 根据ID删除客户（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        customerService.removeById(id);
        return Result.success();
    }
}
