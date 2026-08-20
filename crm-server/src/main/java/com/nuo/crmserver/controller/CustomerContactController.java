package com.nuo.crmserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.CustomerContactQuery;
import com.nuo.crmserver.dto.CustomerContactSaveDTO;
import com.nuo.crmserver.service.CustomerContactService;
import com.nuo.crmserver.vo.CustomerContactVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class CustomerContactController {

    private final CustomerContactService contactService;

    /**
     * 分页查询联系人
     */
    @GetMapping("/page")
    public Result<Page<CustomerContactVO>> page(CustomerContactQuery query) {
        return Result.success(contactService.pageByQuery(query));
    }

    /**
     * 查询某客户的联系人列表（客户详情页内嵌使用）
     */
    @GetMapping("/list/{customerId}")
    public Result<List<CustomerContactVO>> listByCustomer(@PathVariable Long customerId) {
        return Result.success(contactService.listByCustomerId(customerId));
    }

    /**
     * 新增联系人
     */
    @PostMapping
    public Result<Void> saveContact(@Validated @RequestBody CustomerContactSaveDTO dto) {
        contactService.saveContact(dto);
        return Result.success();
    }

    /**
     * 修改联系人
     */
    @PutMapping
    public Result<Void> updateContact(@Validated @RequestBody CustomerContactSaveDTO dto) {
        contactService.updateContact(dto);
        return Result.success();
    }

    /**
     * 删除联系人（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteContact(@PathVariable Long id) {
        contactService.removeById(id);
        return Result.success();
    }
}
