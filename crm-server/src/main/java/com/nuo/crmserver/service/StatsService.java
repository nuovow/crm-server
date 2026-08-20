package com.nuo.crmserver.service;

import com.nuo.crmserver.vo.FunnelVO;
import com.nuo.crmserver.vo.NameValueVO;
import com.nuo.crmserver.vo.StatsOverviewVO;

import java.util.List;

public interface StatsService {

    StatsOverviewVO overview();

    List<NameValueVO> customerLevel();

    List<NameValueVO> customerSource();

    List<FunnelVO> funnel();
}
