package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.CustomerFollowSaveDTO;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.entity.CustomerFollow;
import com.nuo.crmserver.enums.FollowType;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.CustomerFollowMapper;
import com.nuo.crmserver.mapper.CustomerMapper;
import com.nuo.crmserver.service.CustomerFollowService;
import com.nuo.crmserver.vo.CustomerFollowVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFollowServiceImpl extends ServiceImpl<CustomerFollowMapper, CustomerFollow> implements CustomerFollowService {

    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public void addFollow(CustomerFollowSaveDTO dto) {
        Customer customer = customerMapper.selectById(dto.getCustomerId());
        if (customer == null) {
            throw new BizException("客户不存在");
        }
        if (FollowType.of(dto.getFollowType()) == null) {
            throw new BizException("跟进方式只能是电话、拜访、微信、邮件");
        }
        CustomerFollow follow = BeanUtil.copyProperties(dto, CustomerFollow.class);
        follow.setFollowType(FollowType.of(dto.getFollowType()).name());
        save(follow);

        Customer update = new Customer();
        update.setId(customer.getId());
        update.setLastFollowTime(follow.getCreateTime() == null ? java.time.LocalDateTime.now() : follow.getCreateTime());
        customerMapper.updateById(update);
    }

    @Override
    public List<CustomerFollowVO> listByCustomerId(Long customerId) {
        if (customerMapper.selectById(customerId) == null) {
            throw new BizException("客户不存在");
        }
        return baseMapper.selectFollowList(customerId);
    }
}
