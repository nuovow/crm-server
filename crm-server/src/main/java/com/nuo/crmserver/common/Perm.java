package com.nuo.crmserver.common;

/**
 * 权限编码常量：与sys_menu.permission列的值一一对应
 * 用常量引用代替裸字符串，拼写错误在编译期暴露
 */
public interface Perm {
    String CUSTOMER_ADD    = "customer:add";
    String CUSTOMER_EDIT   = "customer:edit";
    String CUSTOMER_DELETE = "customer:delete";
    String CUSTOMER_EXPORT = "customer:export";
    String CUSTOMER_IMPORT = "customer:import";
}
