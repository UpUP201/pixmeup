CREATE TABLE eye_exercise_steps
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    eye_exercise_id BIGINT       NOT NULL,
    step_order      INT          NOT NULL,
    title           VARCHAR(100) NOT NULL,
    instruction     TEXT         NOT NULL,
    step_duration   INT          NOT NULL,
    step_image_url  VARCHAR(255) NOT NULL,
    step_tts_url    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_eye_exercise_steps PRIMARY KEY (id)
);

ALTER TABLE eye_exercise_records
    ADD created_at datetime NULL;

ALTER TABLE eye_exercise_records
    ADD deleted BIT(1) NULL;

ALTER TABLE eye_exercise_records
    ADD updated_at datetime NULL;

ALTER TABLE eye_exercise_records
    MODIFY created_at datetime NOT NULL;

ALTER TABLE eye_exercise_records
    MODIFY deleted BIT (1) NOT NULL;

ALTER TABLE eye_exercise
    ADD guidelines TEXT NULL;

ALTER TABLE eye_exercise
    ADD intro_tts_url VARCHAR(255) NULL;

ALTER TABLE eye_exercise
    ADD precautions TEXT NULL;

ALTER TABLE eye_exercise
    ADD summary TEXT NULL;

ALTER TABLE eye_exercise
    ADD thumbnail_url VARCHAR(255) NULL;

ALTER TABLE eye_exercise
    ADD total_duration INT NULL;

ALTER TABLE eye_exercise
    ADD total_steps INT NULL;

ALTER TABLE eye_exercise
    MODIFY intro_tts_url VARCHAR (255) NOT NULL;

ALTER TABLE eye_exercise
    MODIFY summary TEXT NOT NULL;

ALTER TABLE eye_exercise
    MODIFY thumbnail_url VARCHAR (255) NOT NULL;

ALTER TABLE eye_exercise
    MODIFY total_duration INT NOT NULL;

ALTER TABLE eye_exercise
    MODIFY total_steps INT NOT NULL;

ALTER TABLE eye_exercise_records
    MODIFY updated_at datetime NOT NULL;

ALTER TABLE eye_exercise_steps
    ADD CONSTRAINT FK_EYE_EXERCISE_STEPS_ON_EYE_EXERCISE FOREIGN KEY (eye_exercise_id) REFERENCES eye_exercise (id);

ALTER TABLE eye_exercise
DROP
COLUMN duration;