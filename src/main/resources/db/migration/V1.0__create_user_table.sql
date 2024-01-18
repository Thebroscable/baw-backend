DROP TABLE IF EXISTS user_account CASCADE;

CREATE TABLE user_account (
    id             bigserial NOT NULL,
    email          VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL
);
ALTER TABLE user_account ADD CONSTRAINT user_account_pk PRIMARY KEY ( id );