-- 客户商机表 DDL（M1-T05）：状态表——可增改删，阶段流转驱动统计
CREATE DATABASE IF NOT EXISTS crm_server DEFAULT CHARACTER SET utf8mb4;
USE crm_server;

CREATE TABLE IF NOT EXISTS customer_opportunity (
  id             BIGINT        NOT NULL COMMENT '主键ID(雪花)',
  customer_id    BIGINT        NOT NULL COMMENT '所属客户ID',
  title          VARCHAR(64)   NOT NULL COMMENT '商机名称',
  amount         DECIMAL(12,2) NULL COMMENT '预计成交金额(元)',
  stage          VARCHAR(16)   NOT NULL COMMENT '阶段(初期沟通/方案报价/商务谈判/赢单/输单)',
  expected_date  DATE          NULL COMMENT '预计成交日期',
  remark         VARCHAR(255)  NULL COMMENT '备注',
  create_time    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted        TINYINT       NOT NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
  PRIMARY KEY (id),
  KEY idx_customer_id (customer_id),
  KEY idx_stage (stage)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '客户商机表';
