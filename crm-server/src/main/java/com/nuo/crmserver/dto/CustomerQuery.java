package com.nuo.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
    private String level;
    private String phone;
}
