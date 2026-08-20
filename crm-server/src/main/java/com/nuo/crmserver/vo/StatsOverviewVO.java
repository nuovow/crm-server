package com.nuo.crmserver.vo;

import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

/**
 * 数据看板顶部卡片
 */
@Data
public class StatsOverviewVO {

    private Long customerTotal;
    private Long customerToday;
    private Long customerMonth;
    private Long followToday;

    /** 进行中商机数（不含赢单/输单） */
    private Long opportunityCount;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal opportunityAmount;
}
