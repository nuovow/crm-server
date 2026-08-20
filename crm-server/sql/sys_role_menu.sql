-- M2 用户与权限域：角色-菜单关联表
CREATE TABLE sys_role_menu (
  id          BIGINT   NOT NULL COMMENT '主键ID',
  role_id     BIGINT   NOT NULL COMMENT '角色ID',
  menu_id     BIGINT   NOT NULL COMMENT '菜单ID',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_role_id (role_id),
  KEY idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';
