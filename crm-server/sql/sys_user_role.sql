-- M2 用户与权限域：用户-角色关联表
CREATE TABLE sys_user_role (
  id          BIGINT   NOT NULL COMMENT '主键ID',
  user_id     BIGINT   NOT NULL COMMENT '用户ID',
  role_id     BIGINT   NOT NULL COMMENT '角色ID',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';
