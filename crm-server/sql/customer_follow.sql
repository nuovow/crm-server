-- 客户跟进记录表 DDL（M1-T04）：事件流水表——只插入不更新，无 update_time
CREATE DATABASE IF NOT EXISTS crm_server DEFAULT CHARACTER SET utf8mb4;
USE crm_server;

CREATE TABLE IF NOT EXISTS customer_follow (
  id          BIGINT        NOT NULL COMMENT '主键ID(雪花)',
  customer_id BIGINT        NOT NULL COMMENT '所属客户ID',
  contact_id  BIGINT        NULL COMMENT '联系人ID(可空：不针对具体人)',
  follow_type VARCHAR(16)   NOT NULL COMMENT '跟进方式(电话/拜访/微信/邮件)',
  content     VARCHAR(1000) NOT NULL COMMENT '跟进内容',
  next_time   DATETIME      NULL COMMENT '下次跟进时间',
  create_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间(即跟进发生时间)',
  deleted     TINYINT       NOT NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
  PRIMARY KEY (id),
  KEY idx_customer_id (customer_id),
  KEY idx_next_time (next_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '客户跟进记录表';

-- 客户表补充最后跟进时间（事件驱动状态）
ALTER TABLE customer ADD COLUMN last_follow_time DATETIME NULL COMMENT '最后跟进时间' AFTER remark;
