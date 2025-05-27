ALTER TABLE surveys
DROP
COLUMN gender;

ALTER TABLE surveys
    ADD gender VARCHAR(1) NOT NULL;

ALTER TABLE sight_checks
DROP
COLUMN left_perspective;

ALTER TABLE sight_checks
DROP
COLUMN right_perspective;

ALTER TABLE sight_checks
    ADD left_perspective VARCHAR(255) NOT NULL;

ALTER TABLE sight_checks
    ADD right_perspective VARCHAR(255) NOT NULL;

ALTER TABLE webauthn_credentials
DROP
COLUMN status;

ALTER TABLE webauthn_credentials
    ADD status VARCHAR(255) NOT NULL;