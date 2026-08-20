package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.CustomerContactQuery;
import com.nuo.crmserver.dto.CustomerContactSaveDTO;
import com.nuo.crmserver.entity.CustomerContact;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.CustomerContactMapper;
import com.nuo.crmserver.mapper.CustomerMapper;
import com.nuo.crmserver.service.CustomerContactService;
import com.nuo.crmserver.vo.CustomerContactVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerContactServiceImpl extends ServiceImpl<CustomerContactMapper, CustomerContact> implements CustomerContactService {

    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerContactVO> pageByQuery(CustomerContactQuery query) {
        Page<CustomerContact> page = lambdaQuery()
                .eq(query.getCustomerId() != null, CustomerContact::getCustomerId, query.getCustomerId())
                .like(StrUtil.isNotBlank(query.getName()), CustomerContact::getName, query.getName())
                .orderByDesc(CustomerContact::getCreateTime)
                .page(new Page<>(query.getPageNum(), query.getPageSize()));
        Page<CustomerContactVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(CustomerContactVO::of).toList());
        return voPage;
    }

    @Override
    public List<CustomerContactVO> listByCustomerId(Long customerId) {
        checkCustomerExists(customerId);
        return lambdaQuery()
                .eq(CustomerContact::getCustomerId, customerId)
                .orderByDesc(CustomerContact::getIsPrimary)
                .orderByDesc(CustomerContact::getCreateTime)
                .list()
                .stream()
                .map(CustomerContactVO::of)
                .toList();
    }

    @Override
    @Transactional
    public void saveContact(CustomerContactSaveDTO dto) {
        checkCustomerExists(dto.getCustomerId());
        CustomerContact contact = BeanUtil.copyProperties(dto, CustomerContact.class);
        if (Integer.valueOf(1).equals(dto.getIsPrimary())) {
            clearPrimary(dto.getCustomerId(), null);
        }
        save(contact);
    }

    @Override
    @Transactional
    public void updateContact(CustomerContactSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("联系人ID不能为空");
        }
        CustomerContact exist = getById(dto.getId());
        if (exist == null) {
            throw new BizException("联系人不存在或已删除");
        }
        if (!exist.getCustomerId().equals(dto.getCustomerId())) {
            checkCustomerExists(dto.getCustomerId());
        }
        CustomerContact contact = BeanUtil.copyProperties(dto, CustomerContact.class);
        if (Integer.valueOf(1).equals(dto.getIsPrimary())) {
            clearPrimary(dto.getCustomerId(), dto.getId());
        }
        updateById(contact);
    }

    private void checkCustomerExists(Long customerId) {
        if (customerId == null || customerMapper.selectById(customerId) == null) {
            throw new BizException("客户不存在");
        }
    }

    private void clearPrimary(Long customerId, Long excludeContactId) {
        lambdaUpdate()
                .set(CustomerContact::getIsPrimary, 0)
                .eq(CustomerContact::getCustomerId, customerId)
                .eq(CustomerContact::getIsPrimary, 1)
                .ne(excludeContactId != null, CustomerContact::getId, excludeContactId)
                .update();
    }
}
