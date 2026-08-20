package com.nuo.crmserver.vo;

import com.nuo.crmserver.entity.SysRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysRoleVO {

    private Long id;
    private String name;
    private String code;
    private String remark;
    private LocalDateTime createTime;

    public static SysRoleVO of(SysRole role) {
        SysRoleVO vo = new SysRoleVO();
        vo.setId(role.getId());
        vo.setName(role.getName());
        vo.setCode(role.getCode());
        vo.setRemark(role.getRemark());
        vo.setCreateTime(role.getCreateTime());
        return vo;
    }
}
