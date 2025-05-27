ALTER TABLE presbyopia_checks
    ADD age INT NULL;

ALTER TABLE presbyopia_checks
    ADD age_prediction INT NULL;

ALTER TABLE presbyopia_checks
    ADD ai_result VARCHAR(255) NULL;

ALTER TABLE presbyopia_checks
    MODIFY age INT NOT NULL;

ALTER TABLE amsler_checks
    ADD ai_result VARCHAR(255) NULL;

ALTER TABLE mchart_checks
    ADD ai_result VARCHAR(255) NULL;

ALTER TABLE sight_checks
    ADD ai_result VARCHAR(255) NULL;

ALTER TABLE sight_checks
    ADD left_sight_prediction INT NULL;

ALTER TABLE sight_checks
    ADD right_sight_prediction INT NULL;

ALTER TABLE surveys
    ADD current_smoking BIT(1) NULL;

ALTER TABLE surveys
    ADD past_smoking BIT(1) NULL;

ALTER TABLE surveys
    MODIFY current_smoking BIT (1) NOT NULL;

ALTER TABLE surveys
    MODIFY past_smoking BIT (1) NOT NULL;

ALTER TABLE surveys
DROP
COLUMN smoking;

ALTER TABLE webauthn_credentials
    DROP COLUMN registered_at;