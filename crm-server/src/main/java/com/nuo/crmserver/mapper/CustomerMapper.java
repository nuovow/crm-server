package com.nuo.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuo.crmserver.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
