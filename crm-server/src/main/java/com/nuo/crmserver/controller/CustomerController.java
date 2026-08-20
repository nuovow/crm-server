package com.nuo.crmserver.controller;

import com.nuo.crmserver.Service.CustomerService;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 查询客户列表
     */
    @GetMapping
    public Result<List<Customer>> getAllCustomers(){
        List<Customer> customers = customerService.list();
        return Result.success(customers);
    }

    /**
     * 根据ID查询单个客户
     */
    @GetMapping("/{id}")
    public Result<Customer> getCustomerById(@PathVariable Long id){
        Customer byId = customerService.getById(id);
        return Result.success(byId);
    }

    /**
     * 新增客户
     */
    @PostMapping
    public Result<Void> addCustomer(@RequestBody Customer customer){
        customerService.save(customer);
        return Result.success();
    }

    /**
     * 根据ID修改客户（全量更新）
     */
    @PutMapping
    public Result<Void> updateCustomer(@RequestBody Customer customer){
        customerService.updateById(customer);
        return Result.success();
    }

    /**
     * 根据ID删除客户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id){
        customerService.removeById(id);
        return Result.success();
    }

}