package com.nuo.crmserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    //用户表->角色->用户权限菜单->过滤不是null的

    @Select("""
            SELECT DISTINCT m.permission
            FROM sys_user u
                     JOIN sys_user_role ur ON ur.user_id = u.id
                     JOIN sys_role_menu rm ON rm.role_id = ur.role_id
                     JOIN sys_menu m       ON m.id = rm.menu_id
            WHERE u.id = #{userId}
              AND u.deleted = 0
              AND m.deleted = 0
              AND m.status = 1
              AND m.permission IS NOT NULL;
            """)
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
