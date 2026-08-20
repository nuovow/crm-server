package com.nuo.crmserver.service.impl;

import com.nuo.crmserver.enums.OpportunityStage;
import com.nuo.crmserver.mapper.StatsMapper;
import com.nuo.crmserver.service.StatsService;
import com.nuo.crmserver.vo.FunnelVO;
import com.nuo.crmserver.vo.NameValueVO;
import com.nuo.crmserver.vo.StatsOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    /** 漏斗从上到下的阶段顺序：与商机推进方向一致 */
    private static final List<OpportunityStage> FUNNEL_STAGES = List.of(
            OpportunityStage.INITIAL, OpportunityStage.PROPOSAL, OpportunityStage.NEGOTIATION);

    private final StatsMapper statsMapper;

    @Override
    public StatsOverviewVO overview() {
        StatsOverviewVO vo = new StatsOverviewVO();
        vo.setCustomerTotal(statsMapper.countCustomer());
        vo.setCustomerToday(statsMapper.countCustomerToday());
        vo.setCustomerMonth(statsMapper.countCustomerMonth());
        vo.setFollowToday(statsMapper.countFollowToday());
        vo.setOpportunityCount(statsMapper.countOpportunityOpen());
        vo.setOpportunityAmount(statsMapper.sumOpportunityOpenAmount());
        return vo;
    }

    @Override
    public List<NameValueVO> customerLevel() {
        return statsMapper.countByLevel();
    }

    @Override
    public List<NameValueVO> customerSource() {
        return statsMapper.countBySource();
    }

    @Override
    public List<FunnelVO> funnel() {
        Map<String, FunnelVO> byStage = statsMapper.selectFunnel().stream()
                .collect(Collectors.toMap(FunnelVO::getStage, Function.identity()));
        List<FunnelVO> result = new ArrayList<>();
        for (OpportunityStage stage : FUNNEL_STAGES) {
            FunnelVO vo = byStage.getOrDefault(stage.name(), emptyStage(stage));
            vo.setStageLabel(stage.getLabel());
            result.add(vo);
        }
        return result;
    }

    private FunnelVO emptyStage(OpportunityStage stage) {
        FunnelVO vo = new FunnelVO();
        vo.setStage(stage.name());
        vo.setCount(0L);
        vo.setAmount(BigDecimal.ZERO);
        return vo;
    }
}
