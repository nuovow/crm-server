package com.nuo.crmserver.mapper;

import com.nuo.crmserver.vo.FunnelVO;
import com.nuo.crmserver.vo.NameValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据看板统计查询：聚合SQL不出现在BaseMapper里，独立成专用Mapper
 */
@Mapper
public interface StatsMapper {

    @Select("SELECT COUNT(*) FROM customer WHERE deleted = 0")
    Long countCustomer();

    @Select("SELECT COUNT(*) FROM customer WHERE deleted = 0 AND create_time >= CURDATE()")
    Long countCustomerToday();

    @Select("SELECT COUNT(*) FROM customer WHERE deleted = 0 AND create_time >= DATE_FORMAT(NOW(), '%Y-%m-01')")
    Long countCustomerMonth();

    @Select("SELECT COUNT(*) FROM customer_follow WHERE deleted = 0 AND create_time >= CURDATE()")
    Long countFollowToday();

    @Select("SELECT COUNT(*) FROM customer_opportunity WHERE deleted = 0 AND stage NOT IN ('WON', 'LOST')")
    Long countOpportunityOpen();

    @Select("SELECT IFNULL(SUM(amount), 0) FROM customer_opportunity WHERE deleted = 0 AND stage NOT IN ('WON', 'LOST')")
    BigDecimal sumOpportunityOpenAmount();

    @Select("""
            SELECT level AS name, COUNT(*) AS `value`
            FROM customer
            WHERE deleted = 0 AND level IS NOT NULL
            GROUP BY level
            ORDER BY `value` DESC
            """)
    List<NameValueVO> countByLevel();

    @Select("""
            SELECT source AS name, COUNT(*) AS `value`
            FROM customer
            WHERE deleted = 0 AND source IS NOT NULL
            GROUP BY source
            ORDER BY `value` DESC
            """)
    List<NameValueVO> countBySource();

    @Select("""
            SELECT stage, COUNT(*) AS `count`, SUM(amount) AS amount
            FROM customer_opportunity
            WHERE deleted = 0 AND stage NOT IN ('WON', 'LOST')
            GROUP BY stage
            """)
    List<FunnelVO> selectFunnel();
}
