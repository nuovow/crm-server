package com.nuo.crmserver.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerExport {

    @ExcelProperty("客户姓名")
    @ColumnWidth(15)
    private String name;

    @ExcelProperty("所属行业")
    @ColumnWidth(15)
    private String industry;

    @ExcelProperty("客户来源")
    @ColumnWidth(12)
    private String source;

    @ExcelProperty("客户等级")
    @ColumnWidth(12)
    private String level;

    @ExcelProperty("联系电话")
    @ColumnWidth(15)
    private String phone;

    @ExcelProperty("备注")
    @ColumnWidth(40)
    private String remark;

    @ExcelProperty("最后跟进时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(20)
    private LocalDateTime lastFollowTime;

    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(20)
    private LocalDateTime createTime;
}