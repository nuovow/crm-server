package com.nuo.crmserver.vo;

import com.nuo.crmserver.entity.SysMenu;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuTreeVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long parentId;
    private String name;
    private Integer type;
    private String permission;
    private String path;
    private Integer sort;
    private List<MenuTreeVO> children = new ArrayList<>();

    public static MenuTreeVO of(SysMenu menu) {
        MenuTreeVO vo = new MenuTreeVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setName(menu.getName());
        vo.setType(menu.getType());
        vo.setPermission(menu.getPermission());
        vo.setPath(menu.getPath());
        vo.setSort(menu.getSort());
        return vo;
    }
}
