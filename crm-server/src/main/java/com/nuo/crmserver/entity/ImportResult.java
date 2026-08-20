package com.nuo.crmserver.entity;

import lombok.Data;
import java.util.List;

@Data
public class ImportResult {
    // 文件总数据行数（不含表头）
    private Integer totalRow;
    // 成功入库行数
    private Integer successRow;
    // 失败明细
    private List<String> failList;
}