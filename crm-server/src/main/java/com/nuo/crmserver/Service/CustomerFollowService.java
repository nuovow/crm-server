package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.CustomerFollowSaveDTO;
import com.nuo.crmserver.entity.CustomerFollow;
import com.nuo.crmserver.vo.CustomerFollowVO;

import java.util.List;

public interface CustomerFollowService extends IService<CustomerFollow> {

    void addFollow(CustomerFollowSaveDTO dto);

    List<CustomerFollowVO> listByCustomerId(Long customerId);
}
