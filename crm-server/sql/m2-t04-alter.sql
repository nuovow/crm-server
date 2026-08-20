-- M2-T04: 业务表补审计字段（创建人/更新人），跟进表补跟进人
ALTER TABLE customer
    ADD COLUMN create_by BIGINT NULL COMMENT '创建人ID' AFTER remark,
    ADD COLUMN update_by BIGINT NULL COMMENT '更新人ID' AFTER create_by;

ALTER TABLE customer_contact
    ADD COLUMN create_by BIGINT NULL COMMENT '创建人ID' AFTER is_primary,
    ADD COLUMN update_by BIGINT NULL COMMENT '更新人ID' AFTER create_by;

ALTER TABLE customer_opportunity
    ADD COLUMN create_by BIGINT NULL COMMENT '创建人ID' AFTER remark,
    ADD COLUMN update_by BIGINT NULL COMMENT '更新人ID' AFTER create_by;

ALTER TABLE customer_follow
    ADD COLUMN operator BIGINT NULL COMMENT '跟进人ID' AFTER next_time;
