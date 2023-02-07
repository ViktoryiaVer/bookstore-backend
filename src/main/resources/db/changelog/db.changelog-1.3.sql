--liquibase formatted sql

--changeset viktoryiaver:1
ALTER TABLE authors ADD UNIQUE(first_name, last_name);

--changeset viktoryiaver:2
ALTER TABLE payments ADD UNIQUE(user_id, order_id);

--changeset viktoryiaver:3
ALTER TABLE users ALTER COLUMN login_id SET NOT NULL;

