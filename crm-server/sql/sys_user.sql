-- M2 用户与权限域：系统用户表
CREATE TABLE sys_user (
  id          BIGINT       NOT NULL COMMENT '主键ID(雪花)',
  username    VARCHAR(32)  NOT NULL COMMENT '登录名',
  password    VARCHAR(80)  NOT NULL COMMENT '密码(BCrypt)',
  nickname    VARCHAR(32)  NULL COMMENT '显示名',
  phone       VARCHAR(20)  NULL COMMENT '手机号',
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态(1启用 0禁用)',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0否/1是)',
  PRIMARY KEY (id),
  -- 联合唯一：同一deleted值下username不重复；逻辑删除(deleted=1)后username可重新注册
  UNIQUE KEY uk_username (username, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 初始管理员账号 admin/123456
INSERT INTO sys_user (id, username, password, nickname, phone)
VALUES (1, 'admin',
        '$2a$10$ZVJkaNvvnaYJa32cOPzUy.IgHXhC1W1B7in6U5O1qtpnz/I.IKdHu',
        '系统管理员', '13800000001');
