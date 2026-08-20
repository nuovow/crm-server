package com.nuo.crmserver.task;

import com.nuo.crmserver.mapper.CustomerFollowMapper;
import com.nuo.crmserver.vo.CustomerFollowVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 跟进提醒定时任务：定时线程里没有UserContext，只做查询+日志，不写库
 */
@Component
@RequiredArgsConstructor
public class FollowReminderTask {

    private static final Logger log = LoggerFactory.getLogger(FollowReminderTask.class);

    private final CustomerFollowMapper customerFollowMapper;

    /**
     * 每天早9点扫描"今天该跟进 + 已过期未跟进"的记录并输出提醒日志
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void remindDueFollows() {
        List<CustomerFollowVO> dueList = customerFollowMapper.selectDueFollowList();
        if (dueList.isEmpty()) {
            log.info("[跟进提醒] 今日无到期跟进");
            return;
        }
        log.info("[跟进提醒] 共{}条到期跟进：", dueList.size());
        for (CustomerFollowVO follow : dueList) {
            log.info("[跟进提醒] 客户[{}] 计划时间{} 方式{} 内容：{}",
                    follow.getCustomerName(), follow.getNextTime(),
                    follow.getFollowType(), follow.getContent());
        }
    }
}
