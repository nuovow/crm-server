package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.CustomerQuery;
import com.nuo.crmserver.dto.CustomerSaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.entity.CustomerExport;
import com.nuo.crmserver.entity.ImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService extends IService<Customer> {

    Page<Customer> pageByQuery(CustomerQuery query);

    Customer getCustomerById(Long id);

    void saveCustomer(CustomerSaveDTO dto);

    void updateCustomer(CustomerSaveDTO dto);


    List<CustomerExport> getCustomerList();

    ImportResult importCustomers(MultipartFile file);
}
