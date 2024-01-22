DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE likes (
    id             bigserial NOT NULL,
    user_id        bigserial NOT NULL,
    image_id       bigserial NOT NULL
);

ALTER TABLE likes ADD CONSTRAINT likes_pk PRIMARY KEY ( id );

ALTER TABLE likes
    ADD CONSTRAINT likes_user_fk FOREIGN KEY ( user_id )
        REFERENCES user_account ( id );

ALTER TABLE likes
    ADD CONSTRAINT likes_image_fk FOREIGN KEY ( image_id )
        REFERENCES image ( id );