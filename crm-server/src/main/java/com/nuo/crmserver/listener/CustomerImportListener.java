package com.nuo.crmserver.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import cn.hutool.core.bean.BeanUtil;
import com.nuo.crmserver.entity.Customer;
import com.nuo.crmserver.entity.CustomerImport;
import com.nuo.crmserver.service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomerImportListener extends AnalysisEventListener<CustomerImport> {
    // 分批阈值：攒多少条执行一次批量入库
    private static final int BATCH_SIZE = 100;

    // 缓存：校验通过、无重复的数据
    private final List<CustomerImport> validDataList = new ArrayList<>(BATCH_SIZE);
    // 缓存：所有失败信息
    private final List<String> failList = new ArrayList<>();
    // 已存在客户名称集合（提前传入，用于去重判断）
    private final Set<String> existCustomerNameSet;
    // service：用于批量入库
    private final CustomerService customerService;
    // 有效数据总行计数
    private int totalRow = 0;

    // 构造方法：注入依赖 + 已存在名称集合
    public CustomerImportListener(CustomerService customerService, Set<String> existCustomerNameSet) {
        this.customerService = customerService;
        this.existCustomerNameSet = existCustomerNameSet;
    }

    /**
     * 【核心回调】每读取Excel一行数据，执行一次
     * @param row 当前行映射后的实体
     * @param context 上下文：可以拿到Excel行号
     */
    @Override
    public void invoke(CustomerImport row, AnalysisContext context) {
        totalRow++;
        // 获取Excel原始行下标（从0开始，表头占第0行）
        int excelIndex = context.readRowHolder().getRowIndex();
        // 转换：用户在Excel肉眼看到号的行（表头第1行，数据从第2行开始）
        int showRowNum = excelIndex + 1;

        // 1. 单行字段校验
        String validateMsg = row.validate();
        if (validateMsg != null) {
            failList.add("第" + showRowNum + "行：" + validateMsg);
            return;
        }
        // 2. 业务去重校验（含文件内部重复：加入集合后，同名后续行自动被拦）
        if (existCustomerNameSet.contains(row.getName())) {
            failList.add("第" + showRowNum + "行：客户【" + row.getName() + "】已存在，跳过导入");
            return;
        }
        existCustomerNameSet.add(row.getName());
        // ✅ 当前行合法，加入缓存
        validDataList.add(row);
        // 缓存达到阈值 → 执行批量入库，清空缓存
        if (validDataList.size() >= BATCH_SIZE) {
            batchSave();
            validDataList.clear();
        }
    }

    /**
     * 【收尾回调】整个Excel全部读取完成后执行一次
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 处理缓存中剩余不足一批的数据
        batchSave();
    }

    /**
     * 批量保存合法数据：Excel行对象转换为PO后入库
     */
    private void batchSave() {
        if (!validDataList.isEmpty()) {
            List<Customer> customers = validDataList.stream()
                    .map(row -> BeanUtil.copyProperties(row, Customer.class))
                    .toList();
            customerService.saveBatch(customers, BATCH_SIZE);
        }
    }

    // ========== 对外提供getter，读取导入汇总结果 ==========
    public List<String> getFailList() {
        return failList;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public Integer getSuccessRow() {
        return totalRow - failList.size();
    }
}