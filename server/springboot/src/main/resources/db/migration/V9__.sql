ALTER TABLE report_summaries
    ADD created_at datetime NULL;

ALTER TABLE report_summaries
    ADD deleted BIT(1) NULL;

ALTER TABLE report_summaries
    ADD updated_at datetime NULL;

ALTER TABLE report_summaries
    MODIFY created_at datetime NOT NULL;

ALTER TABLE report_summaries
    MODIFY deleted BIT (1) NOT NULL;

ALTER TABLE users
    ADD external_key VARCHAR(64) NULL;

ALTER TABLE users
    ADD withdrawal_at datetime NULL;

ALTER TABLE webauthn_credentials
    ADD registered_at datetime NULL;

ALTER TABLE webauthn_credentials
    MODIFY registered_at datetime NOT NULL;

ALTER TABLE report_summaries
    MODIFY updated_at datetime NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT uc_users_externalkey UNIQUE (external_key);

ALTER TABLE webauthn_credentials
DROP
COLUMN created_at;

ALTER TABLE webauthn_credentials
DROP
COLUMN deleted;

ALTER TABLE webauthn_credentials
DROP
COLUMN updated_at;