package com.nuo.crmserver.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class CustomerImport {

    @ExcelProperty("客户名称")
    private String name;

    @ExcelProperty("所属行业")
    private String industry;

    @ExcelProperty("客户来源")
    private String source;

    @ExcelProperty("客户级别")
    private String level;

    @ExcelProperty("联系电话")
    private String phone;

    @ExcelProperty("备注")
    private String remark;

    /**
     * 单行数据校验，返回null=校验通过；返回字符串=错误信息
     */
    public String validate() {
        if (StrUtil.isBlank(name)) {
            return "客户名称不能为空";
        }
        if (StrUtil.isNotBlank(level) && !level.matches("[ABC]")) {
            return "客户级别仅支持 A/B/C";
        }
        if (StrUtil.isNotBlank(phone) && !phone.matches("^1[3-9]\\d{9}$")) {
            return "手机号格式非法";
        }
        return null;
    }
}
