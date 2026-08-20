package com.nuo.crmserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFollow {

    @TableId
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
    @TableLogic
    private Integer deleted;
}
