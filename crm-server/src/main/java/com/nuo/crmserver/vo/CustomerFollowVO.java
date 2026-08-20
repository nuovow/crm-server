package com.nuo.crmserver.vo;

import cn.hutool.core.bean.BeanUtil;
import com.nuo.crmserver.entity.CustomerFollow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFollowVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long contactId;

    private String followType;
    private String content;
    private LocalDateTime nextTime;
    private LocalDateTime createTime;

    public static CustomerFollowVO of(CustomerFollow follow) {
        CustomerFollowVO vo = new CustomerFollowVO();
        BeanUtil.copyProperties(follow, vo);
        return vo;
    }
}
