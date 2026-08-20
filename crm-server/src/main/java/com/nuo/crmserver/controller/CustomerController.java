package com.nuo.crmserver.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.CustomerQuery;
import com.nuo.crmserver.dto.CustomerSaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.entity.CustomerExport;
import com.nuo.crmserver.entity.ImportResult;
import com.nuo.crmserver.service.CustomerService;
import com.nuo.crmserver.vo.CustomerVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/export")
    public void exportCustomer(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=customers.xlsx");
        List<CustomerExport> customerExports = customerService.getCustomerList();
        EasyExcel.write(response.getOutputStream(), CustomerExport.class)
                .sheet("客户列表")
                .doWrite(customerExports);
    }

    /**
     * Excel批量导入客户：部分成功语义——合法行入库，非法行返回明细
     */
    @PostMapping("/import")
    public Result<ImportResult> importCustomers(@RequestParam("file") MultipartFile file) {
        return Result.success(customerService.importCustomers(file));
    }
}
