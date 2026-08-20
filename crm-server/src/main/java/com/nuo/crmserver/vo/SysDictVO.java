package com.nuo.crmserver.vo;

import com.nuo.crmserver.entity.SysDict;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
public class SysDictVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String type;
    private String label;
    private String value;
    private Integer sort;
    private String remark;
    private LocalDateTime createTime;

    public static SysDictVO of(SysDict dict) {
        SysDictVO vo = new SysDictVO();
        vo.setId(dict.getId());
        vo.setType(dict.getType());
        vo.setLabel(dict.getLabel());
        vo.setValue(dict.getValue());
        vo.setSort(dict.getSort());
        vo.setRemark(dict.getRemark());
        vo.setCreateTime(dict.getCreateTime());
        return vo;
    }
}
