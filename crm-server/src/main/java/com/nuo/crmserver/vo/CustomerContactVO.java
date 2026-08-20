package com.nuo.crmserver.vo;

import cn.hutool.core.bean.BeanUtil;
import com.nuo.crmserver.entity.CustomerContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerContactVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    private String name;
    private String position;
    private String phone;
    private String wechat;
    private Integer isPrimary;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static CustomerContactVO of(CustomerContact contact) {
        CustomerContactVO vo = new CustomerContactVO();
        BeanUtil.copyProperties(contact, vo);
        return vo;
    }
}
