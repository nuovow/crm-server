package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.CustomerQuery;
import com.nuo.crmserver.dto.CustomerSaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.entity.CustomerExport;
import com.nuo.crmserver.entity.CustomerImport;
import com.nuo.crmserver.entity.ImportResult;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.listener.CustomerImportListener;
import com.nuo.crmserver.mapper.CustomerMapper;
import com.nuo.crmserver.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    public Page<Customer> pageByQuery(CustomerQuery query) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(query.getName()), Customer::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getLevel()), Customer::getLevel, query.getLevel())
                .like(StrUtil.isNotBlank(query.getPhone()), Customer::getPhone, query.getPhone())
                .orderByDesc(Customer::getCreateTime)
                .page(new Page<>(query.getPageNum(), query.getPageSize()));
    }

    @Override
    public Customer getCustomerById(Long id) {
        Customer customer = getById(id);
        if (customer == null) {
            throw new BizException("客户不存在");
        }
        return customer;
    }

    @Override
    public void saveCustomer(CustomerSaveDTO dto) {
        save(BeanUtil.copyProperties(dto, Customer.class));
    }

    @Override
    public void updateCustomer(CustomerSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("客户ID不能为空");
        }
        boolean updated = updateById(BeanUtil.copyProperties(dto, Customer.class));
        if (!updated) {
            throw new BizException("客户不存在或已删除");
        }
    }

    @Override
    public List<CustomerExport> getCustomerList() {
        List<Customer> customers = this.list();
        return BeanUtil.copyToList(customers, CustomerExport.class);
    }

    @Override
    public ImportResult importCustomers(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            throw new BizException("仅支持.xlsx格式文件");
        }
        // 一次性查出已存在客户名，循环内contains为O(1)，避免N+1
        Set<String> existNames = list().stream()
                .map(Customer::getName)
                .collect(Collectors.toSet());

        CustomerImportListener listener = new CustomerImportListener(this, existNames);
        try {
            EasyExcel.read(file.getInputStream(), CustomerImport.class, listener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new BizException("文件读取失败，请检查文件是否损坏");
        }

        ImportResult result = new ImportResult();
        result.setTotalRow(listener.getTotalRow());
        result.setSuccessRow(listener.getSuccessRow());
        result.setFailList(listener.getFailList());
        return result;
    }

}
