package com.nuo.crmserver.vo;

import com.nuo.crmserver.entity.SysUser;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
public class SysUserVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String username;
    private String nickname;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;

    public static SysUserVO of(SysUser user) {
        SysUserVO vo = new SysUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setPhone(user.getPhone());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}
