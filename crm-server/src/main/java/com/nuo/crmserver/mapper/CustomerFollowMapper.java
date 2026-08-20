package com.nuo.crmserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuo.crmserver.entity.CustomerFollow;
import com.nuo.crmserver.vo.CustomerFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerFollowMapper extends BaseMapper<CustomerFollow> {

    @Select("""
            SELECT f.id, f.customer_id AS customerId, f.contact_id AS contactId,
                   f.follow_type AS followType, f.content, f.next_time AS nextTime,
                   f.create_time AS createTime, f.operator,
                   u.nickname AS operatorName
            FROM customer_follow f
            LEFT JOIN sys_user u ON u.id = f.operator
            WHERE f.customer_id = #{customerId} AND f.deleted = 0
            ORDER BY f.create_time DESC
            """)
    List<CustomerFollowVO> selectFollowList(@Param("customerId") Long customerId);

    @Select("""
            SELECT f.id, f.customer_id AS customerId, f.contact_id AS contactId,
                   f.follow_type AS followType, f.content, f.next_time AS nextTime,
                   f.create_time AS createTime, f.operator,
                   u.nickname AS operatorName, c.name AS customerName
            FROM customer_follow f
            LEFT JOIN sys_user u ON u.id = f.operator
            LEFT JOIN customer c ON c.id = f.customer_id
            WHERE f.deleted = 0
              AND f.next_time >= CURDATE()
              AND f.next_time < CURDATE() + INTERVAL 1 DAY
            ORDER BY f.next_time
            """)
    List<CustomerFollowVO> selectTodayFollowList();

    @Select("""
            SELECT f.id, f.customer_id AS customerId, f.contact_id AS contactId,
                   f.follow_type AS followType, f.content, f.next_time AS nextTime,
                   f.create_time AS createTime, f.operator,
                   u.nickname AS operatorName, c.name AS customerName
            FROM customer_follow f
            LEFT JOIN sys_user u ON u.id = f.operator
            LEFT JOIN customer c ON c.id = f.customer_id
            WHERE f.deleted = 0 AND c.deleted = 0
              AND f.next_time IS NOT NULL
              AND f.next_time < CURDATE() + INTERVAL 1 DAY
            ORDER BY f.next_time
            """)
    List<CustomerFollowVO> selectDueFollowList();
}
