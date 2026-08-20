package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.CustomerQuery;
import com.nuo.crmserver.dto.CustomerSaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.CustomerMapper;
import com.nuo.crmserver.service.CustomerService;
import org.springframework.stereotype.Service;

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
}
