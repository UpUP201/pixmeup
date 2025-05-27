ALTER TABLE eye_exercise
    MODIFY `description` TEXT NOT NULL;

ALTER TABLE sight_checks
    MODIFY left_perspective VARCHAR (255) NULL;

ALTER TABLE sight_checks
    MODIFY right_perspective VARCHAR (255) NULL;