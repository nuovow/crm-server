package com.nuo.crmserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.nuo.crmserver.dto.DictSaveDTO;
import com.nuo.crmserver.entity.SysDict;
import com.nuo.crmserver.exceptions.BizException;
import com.nuo.crmserver.mapper.SysDictMapper;
import com.nuo.crmserver.service.SysDictService;
import com.nuo.crmserver.vo.SysDictVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public List<SysDictVO> listByType(String type) {
        return lambdaQuery()
                .eq(SysDict::getType, type)
                .orderByAsc(SysDict::getSort)
                .list()
                .stream().map(SysDictVO::of).toList();
    }

    @Override
    public List<SysDictVO> listAll() {
        return lambdaQuery()
                .orderByAsc(SysDict::getType, SysDict::getSort)
                .list()
                .stream().map(SysDictVO::of).toList();
    }

    @Override
    public void saveDict(DictSaveDTO dto) {
        checkUnique(dto.getType(), dto.getValue(), null);
        save(BeanUtil.copyProperties(dto, SysDict.class));
    }

    @Override
    public void updateDict(DictSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BizException("字典ID不能为空");
        }
        checkUnique(dto.getType(), dto.getValue(), dto.getId());
        if (!updateById(BeanUtil.copyProperties(dto, SysDict.class))) {
            throw new BizException("字典项不存在");
        }
    }

    @Override
    public void deleteDict(Long id) {
        if (!removeById(id)) {
            throw new BizException("字典项不存在");
        }
    }

    /**
     * 同类型下存储值唯一：uk_type_value兜底，业务层先拦给出友好提示
     */
    private void checkUnique(String type, String value, Long excludeId) {
        SysDict existing = lambdaQuery()
                .eq(SysDict::getType, type)
                .eq(SysDict::getValue, value)
                .one();
        if (existing != null && !existing.getId().equals(excludeId)) {
            throw new BizException("该类型下存储值已存在");
        }
    }
}
