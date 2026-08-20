package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.service.StatsService;
import com.nuo.crmserver.vo.FunnelVO;
import com.nuo.crmserver.vo.NameValueVO;
import com.nuo.crmserver.vo.StatsOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * 看板顶部卡片：客户/跟进/商机总量与增量
     */
    @GetMapping("/overview")
    public Result<StatsOverviewVO> overview() {
        return Result.success(statsService.overview());
    }

    /**
     * 客户等级分布（饼图）
     */
    @GetMapping("/customer/level")
    public Result<List<NameValueVO>> customerLevel() {
        return Result.success(statsService.customerLevel());
    }

    /**
     * 客户来源分布（饼图）
     */
    @GetMapping("/customer/source")
    public Result<List<NameValueVO>> customerSource() {
        return Result.success(statsService.customerSource());
    }

    /**
     * 销售漏斗：进行中商机按阶段统计数量与金额
     */
    @GetMapping("/funnel")
    public Result<List<FunnelVO>> funnel() {
        return Result.success(statsService.funnel());
    }
}
