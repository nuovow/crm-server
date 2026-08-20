-- 客户联系人表 DDL（M1-T03）
CREATE DATABASE IF NOT EXISTS crm_server DEFAULT CHARACTER SET utf8mb4;
USE crm_server;

CREATE TABLE IF NOT EXISTS customer_contact (
  id          BIGINT       NOT NULL COMMENT '主键ID(雪花)',
  customer_id BIGINT       NOT NULL COMMENT '所属客户ID',
  name        VARCHAR(32)  NOT NULL COMMENT '姓名',
  position    VARCHAR(32)  NULL COMMENT '职务',
  phone       VARCHAR(20)  NOT NULL COMMENT '手机号',
  wechat      VARCHAR(64)  NULL COMMENT '微信号',
  is_primary  TINYINT      NOT NULL DEFAULT 0 COMMENT '是否首要联系人 0否 1是',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除 0未删 1已删',
  PRIMARY KEY (id),
  KEY idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '客户联系人表';
