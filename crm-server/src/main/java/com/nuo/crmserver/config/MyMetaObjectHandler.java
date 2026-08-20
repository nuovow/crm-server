package com.nuo.crmserver.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.nuo.crmserver.common.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 审计字段自动填充：insert时填createBy/updateBy/operator，update时填updateBy
 * strictXxxFill只作用于"实体里声明了fill注解的同名字段"，实体没有该字段时静默跳过
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return;
        }
        strictInsertFill(metaObject, "createBy", Long.class, userId);
        strictInsertFill(metaObject, "updateBy", Long.class, userId);
        strictInsertFill(metaObject, "operator", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return;
        }
        strictUpdateFill(metaObject, "updateBy", Long.class, userId);
    }
}
