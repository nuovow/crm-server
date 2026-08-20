package com.nuo.crmserver.vo;

import cn.hutool.core.bean.BeanUtil;
import com.nuo.crmserver.entity.CustomerOpportunity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOpportunityVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    private String title;
    private BigDecimal amount;
    private String stage;
    private LocalDate expectedDate;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static CustomerOpportunityVO of(CustomerOpportunity opportunity) {
        CustomerOpportunityVO vo = new CustomerOpportunityVO();
        BeanUtil.copyProperties(opportunity, vo);
        return vo;
    }
}
