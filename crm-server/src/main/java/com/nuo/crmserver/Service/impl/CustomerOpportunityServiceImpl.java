package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.CustomerOpportunityQuery;
import com.nuo.crmserver.dto.CustomerOpportunitySaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.entity.CustomerOpportunity;
import com.nuo.crmserver.enums.OpportunityStage;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.CustomerMapper;
import com.nuo.crmserver.mapper.CustomerOpportunityMapper;
import com.nuo.crmserver.service.CustomerOpportunityService;
import com.nuo.crmserver.vo.CustomerOpportunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOpportunityServiceImpl extends ServiceImpl<CustomerOpportunityMapper, CustomerOpportunity> implements CustomerOpportunityService {

    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerOpportunityVO> pageByQuery(CustomerOpportunityQuery query) {
        Page<CustomerOpportunity> page = lambdaQuery()
                .eq(query.getCustomerId() != null, CustomerOpportunity::getCustomerId, query.getCustomerId())
                .like(StrUtil.isNotBlank(query.getTitle()), CustomerOpportunity::getTitle, query.getTitle())
                .eq(StrUtil.isNotBlank(query.getStage()), CustomerOpportunity::getStage, query.getStage())
                .orderByDesc(CustomerOpportunity::getCreateTime)
                .page(new Page<>(query.getPageNum(), query.getPageSize()));
        Page<CustomerOpportunityVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(CustomerOpportunityVO::of).toList());
        return voPage;
    }

    @Override
    public List<CustomerOpportunityVO> listByCustomerId(Long customerId) {
        checkCustomerExists(customerId);
        return lambdaQuery()
                .eq(CustomerOpportunity::getCustomerId, customerId)
                .orderByDesc(CustomerOpportunity::getAmount)
                .list()
                .stream()
                .map(CustomerOpportunityVO::of)
                .toList();
    }

    @Override
    public void saveOpportunity(CustomerOpportunitySaveDTO dto) {
        checkCustomerExists(dto.getCustomerId());
        CustomerOpportunity opportunity = BeanUtil.copyProperties(dto, CustomerOpportunity.class);
        opportunity.setStage(normalizeStage(dto.getStage()));
        save(opportunity);
    }

    @Override
    public void updateOpportunity(CustomerOpportunitySaveDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("商机ID不能为空");
        }
        if (getById(dto.getId()) == null) {
            throw new BizException("商机不存在或已删除");
        }
        checkCustomerExists(dto.getCustomerId());
        CustomerOpportunity opportunity = BeanUtil.copyProperties(dto, CustomerOpportunity.class);
        opportunity.setStage(normalizeStage(dto.getStage()));
        updateById(opportunity);
    }

    private void checkCustomerExists(Long customerId) {
        if (customerId == null || customerMapper.selectById(customerId) == null) {
            throw new BizException("客户不存在");
        }
    }

    private String normalizeStage(String stage) {
        OpportunityStage parsed = OpportunityStage.of(stage);
        if (parsed == null) {
            throw new BizException("商机阶段只能是初期沟通、方案报价、商务谈判、赢单、输单");
        }
        return parsed.name();
    }
}
