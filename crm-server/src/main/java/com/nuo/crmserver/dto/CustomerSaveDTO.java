package com.nuo.crmserver.dto;

import lombok.Data;

@Data
public class CustomerSaveDTO {

    private Long id;

    private String name;
    private String industry;
    private String source;
    private String level;
    private String phone;
    private String remark;
}
