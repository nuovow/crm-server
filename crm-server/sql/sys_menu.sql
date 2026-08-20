-- M2 用户与权限域：菜单权限表
CREATE TABLE sys_menu (
  id          BIGINT       NOT NULL COMMENT '主键ID(雪花)',
  parent_id   BIGINT       NOT NULL DEFAULT 0 COMMENT '父菜单ID(0=顶级)',
  name        VARCHAR(32)  NOT NULL COMMENT '菜单/按钮名',
  type        TINYINT      NOT NULL COMMENT '类型(1菜单 2按钮)',
  permission  VARCHAR(64)  NULL COMMENT '权限编码(如customer:delete，菜单可为空)',
  path        VARCHAR(128) NULL COMMENT '前端路由地址(菜单用)',
  sort        INT          NOT NULL DEFAULT 0 COMMENT '显示顺序',
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态(1启用 0禁用)',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0否/1是)',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 菜单树种子数据：目录→菜单→按钮三层
INSERT INTO sys_menu (id, parent_id, name, type, permission, path, sort) VALUES
-- 客户管理目录
(100, 0,   '客户管理', 1, NULL,            NULL,        1),
-- 客户列表菜单
(101, 100, '客户列表', 1, NULL,            '/customer', 1),
-- 客户列表下的按钮
(102, 101, '新增客户', 2, 'customer:add',    NULL, 1),
(103, 101, '编辑客户', 2, 'customer:edit',   NULL, 2),
(104, 101, '删除客户', 2, 'customer:delete', NULL, 3),
(105, 101, '导出客户', 2, 'customer:export', NULL, 4),
(106, 101, '导入客户', 2, 'customer:import', NULL, 5),
-- 用户管理目录
(200, 0,   '用户管理', 1, NULL,          NULL,     2),
(201, 200, '用户列表', 1, NULL,          '/user',  1);
