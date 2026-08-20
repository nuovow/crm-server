-- M2 用户与权限域：角色表
CREATE TABLE sys_role (
  id          BIGINT       NOT NULL COMMENT '主键ID(雪花)',
  name        VARCHAR(32)  NOT NULL COMMENT '角色名(如:销售)',
  code        VARCHAR(32)  NOT NULL COMMENT '角色编码(如:SALES)',
  remark      VARCHAR(255) NULL COMMENT '备注',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0否/1是)',
  PRIMARY KEY (id),
  -- 联合唯一：同一deleted值下code不重复；逻辑删除后code可复用
  UNIQUE KEY uk_code (code, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 初始角色：管理员/销售/销售经理
INSERT INTO sys_role (id, name, code, remark) VALUES
(1, '管理员',   'ADMIN',  '系统管理，用户与角色分配'),
(2, '销售',     'SALES',  '一线业务，维护自己的客户'),
(3, '销售经理', 'MANAGER', '团队管理，查看全组数据');
