package com.nuo.crmserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用name/value对：饼图、柱状图的直接数据源
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameValueVO {

    private String name;
    private Long value;
}
