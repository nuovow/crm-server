-- 客户表 DDL（M0-T03）
CREATE DATABASE IF NOT EXISTS crm_server DEFAULT CHARACTER SET utf8mb4;
USE crm_server;

CREATE TABLE IF NOT EXISTS customer (
  id          BIGINT       NOT NULL COMMENT '主键(雪花ID)',
  name        VARCHAR(64)  NOT NULL COMMENT '客户名称',
  industry    VARCHAR(32)  NULL COMMENT '行业',
  source      VARCHAR(32)  NULL COMMENT '客户来源',
  level       VARCHAR(8)   NULL COMMENT '客户级别(A/B/C)',
  phone       VARCHAR(20)  NULL COMMENT '联系电话',
  remark      VARCHAR(255) NULL COMMENT '备注',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0否/1是)',
  PRIMARY KEY (id),
  KEY idx_name (name)
) ENGINE = InnoDB COMMENT '客户表';
