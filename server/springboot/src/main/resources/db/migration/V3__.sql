ALTER TABLE users
    ADD password VARCHAR(255) NULL;

ALTER TABLE users
    MODIFY password VARCHAR (255) NOT NULL;