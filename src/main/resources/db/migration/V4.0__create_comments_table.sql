DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE comments (
    id             bigserial NOT NULL,
    text           TEXT NOT NULL,
    user_id        bigserial NOT NULL,
    image_id       bigserial NOT NULL,
    date_time      TIMESTAMP NOT NULL
);

ALTER TABLE comments ADD CONSTRAINT comments_pk PRIMARY KEY ( id );

ALTER TABLE comments
    ADD CONSTRAINT comments_user_fk FOREIGN KEY ( user_id )
        REFERENCES user_account ( id );

ALTER TABLE comments
    ADD CONSTRAINT comments_image_fk FOREIGN KEY ( image_id )
        REFERENCES image ( id );