package com.nuo.crmserver.controller;

import com.nuo.crmserver.common.Perm;
import com.nuo.crmserver.common.RequirePermission;
import com.nuo.crmserver.common.Result;
import com.nuo.crmserver.dto.DictSaveDTO;
import com.nuo.crmserver.service.SysDictService;
import com.nuo.crmserver.vo.SysDictVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictService sysDictService;

    /**
     * 按类型查字典项：客户表单的等级/来源下拉框数据源，登录即可用
     */
    @GetMapping("/type/{type}")
    public Result<List<SysDictVO>> listByType(@PathVariable String type) {
        return Result.success(sysDictService.listByType(type));
    }

    /**
     * 全量列表：字典管理界面
     */
    @GetMapping("/list")
    public Result<List<SysDictVO>> list() {
        return Result.success(sysDictService.listAll());
    }

    @RequirePermission(Perm.DICT_ADD)
    @PostMapping
    public Result<Void> save(@Validated @RequestBody DictSaveDTO dto) {
        sysDictService.saveDict(dto);
        return Result.success();
    }

    @RequirePermission(Perm.DICT_EDIT)
    @PutMapping
    public Result<Void> update(@Validated @RequestBody DictSaveDTO dto) {
        sysDictService.updateDict(dto);
        return Result.success();
    }

    @RequirePermission(Perm.DICT_DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysDictService.deleteDict(id);
        return Result.success();
    }
}
