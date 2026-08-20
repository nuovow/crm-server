package com.nuo.crmserver.vo;

import cn.hutool.core.bean.BeanUtil;
import com.nuo.crmserver.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;
    private String industry;
    private String source;
    private String level;
    private String phone;
    private String remark;
    private LocalDateTime lastFollowTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static CustomerVO of(Customer customer) {
        CustomerVO vo = new CustomerVO();
        BeanUtil.copyProperties(customer, vo);
        return vo;
    }
}
