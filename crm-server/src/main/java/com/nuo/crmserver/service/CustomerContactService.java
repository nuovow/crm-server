package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.CustomerContactQuery;
import com.nuo.crmserver.dto.CustomerContactSaveDTO;
import com.nuo.crmserver.entity.CustomerContact;
import com.nuo.crmserver.vo.CustomerContactVO;

import java.util.List;

public interface CustomerContactService extends IService<CustomerContact> {

    Page<CustomerContactVO> pageByQuery(CustomerContactQuery query);

    List<CustomerContactVO> listByCustomerId(Long customerId);

    void saveContact(CustomerContactSaveDTO dto);

    void updateContact(CustomerContactSaveDTO dto);
}
