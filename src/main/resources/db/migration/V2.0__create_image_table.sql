DROP TABLE IF EXISTS image CASCADE;

CREATE TABLE image (
    id             bigserial NOT NULL,
    title          VARCHAR(255) NOT NULL,
    description    TEXT NOT NULL,
    file_name      VARCHAR(255) NOT NULL,
    user_id        bigserial NOT NULL,
    sum_likes      bigserial NOT NULL,
    date_time      TIMESTAMP NOT NULL
);

ALTER TABLE image ADD CONSTRAINT image_pk PRIMARY KEY ( id );

ALTER TABLE image
    ADD CONSTRAINT image_user_fk FOREIGN KEY ( user_id )
        REFERENCES user_account ( id );