package com.nuo.crmserver.vo;

import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

/**
 * 销售漏斗单层：阶段名 + 商机数 + 金额合计
 */
@Data
public class FunnelVO {

    private String stage;
    private String stageLabel;
    private Long count;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal amount;
}
