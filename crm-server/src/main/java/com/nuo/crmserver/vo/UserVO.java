package com.nuo.crmserver.vo;

import cn.hutool.core.bean.BeanUtil;
import com.nuo.crmserver.entity.SysUser;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象：刻意不含password字段，密码永不外泄
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;

    public static UserVO of(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }
}
