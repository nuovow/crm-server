package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.CustomerOpportunityQuery;
import com.nuo.crmserver.dto.CustomerOpportunitySaveDTO;
import com.nuo.crmserver.entity.CustomerOpportunity;
import com.nuo.crmserver.vo.CustomerOpportunityVO;

import java.util.List;

public interface CustomerOpportunityService extends IService<CustomerOpportunity> {

    Page<CustomerOpportunityVO> pageByQuery(CustomerOpportunityQuery query);

    List<CustomerOpportunityVO> listByCustomerId(Long customerId);

    void saveOpportunity(CustomerOpportunitySaveDTO dto);

    void updateOpportunity(CustomerOpportunitySaveDTO dto);
}
