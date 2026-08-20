package com.nuo.crmserver.dto;

import lombok.Data;

@Data
public class CustomerOpportunityQuery {

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    private Long customerId;
    private String title;
    private String stage;
}
