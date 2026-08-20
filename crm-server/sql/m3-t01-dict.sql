-- M3-T01: 数据字典表 + 种子数据 + 菜单权限
USE crm_server;

CREATE TABLE sys_dict (
  id          BIGINT       NOT NULL COMMENT '主键ID(雪花)',
  type        VARCHAR(64)  NOT NULL COMMENT '字典类型(customer_level/customer_source)',
  label       VARCHAR(64)  NOT NULL COMMENT '显示文本，如"A级（重点客户）"',
  value       VARCHAR(64)  NOT NULL COMMENT '存储值，如"A"',
  sort        INT          NOT NULL DEFAULT 0 COMMENT '显示顺序',
  remark      VARCHAR(255) NULL COMMENT '备注',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0否/1是)',
  PRIMARY KEY (id),
  UNIQUE KEY uk_type_value (type, value, deleted),
  KEY idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典表';

-- 种子数据：level的value与customer.level列取值对齐；source的value与现有测试数据中文取值对齐
INSERT INTO sys_dict (type, label, value, sort) VALUES
('customer_level', 'A级（重点客户）', 'A', 1),
('customer_level', 'B级（普通客户）', 'B', 2),
('customer_level', 'C级（潜在客户）', 'C', 3),
('customer_level', 'D级（无效客户）', 'D', 4),
('customer_source', '官网留言', '官网留言', 1),
('customer_source', '展会获客', '展会获客', 2),
('customer_source', '老客户转介绍', '老客户转介绍', 3),
('customer_source', '广告投放', '广告投放', 4),
('customer_source', '电话陌拜', '电话陌拜', 5);

-- 菜单：字典管理挂在用户管理目录(200)下，与用户列表(201)/角色管理(202)并列
INSERT INTO sys_menu (id, parent_id, name, type, permission, path, sort) VALUES
(203, 200, '字典管理', 1, NULL, '/dict', 3),
(2031, 203, '新增字典', 2, 'dict:add', NULL, 1),
(2032, 203, '编辑字典', 2, 'dict:edit', NULL, 2),
(2033, 203, '删除字典', 2, 'dict:delete', NULL, 3);

-- 字典管理只授给ADMIN(1)；id从28起接续既有种子
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES
(28, 1, 203), (29, 1, 2031), (30, 1, 2032), (31, 1, 2033);
