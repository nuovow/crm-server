package com.nuo.crmserver.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.nuo.crmserver.dto.DictSaveDTO;
import com.nuo.crmserver.entity.SysDict;
import com.nuo.crmserver.vo.SysDictVO;

import java.util.List;

public interface SysDictService extends IService<SysDict> {

    /** 按类型查字典项：表单下拉框的数据源 */
    List<SysDictVO> listByType(String type);

    /** 全量分页：字典管理界面 */
    List<SysDictVO> listAll();

    void saveDict(DictSaveDTO dto);

    void updateDict(DictSaveDTO dto);

    void deleteDict(Long id);
}
