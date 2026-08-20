package com.nuo.crmserver.Service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.Service.CustomerService;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
}
