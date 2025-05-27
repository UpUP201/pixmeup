ALTER TABLE user_agrees
    ADD consignment BIT(1) NULL;

ALTER TABLE user_agrees
    ADD marketing BIT(1) NULL;

ALTER TABLE user_agrees
    ADD sensitive_info BIT(1) NULL;

ALTER TABLE user_agrees
    ADD third_party BIT(1) NULL;

ALTER TABLE user_agrees
    MODIFY consignment BIT (1) NOT NULL;

ALTER TABLE user_agrees
    MODIFY marketing BIT (1) NOT NULL;

ALTER TABLE user_agrees
    MODIFY sensitive_info BIT (1) NOT NULL;

ALTER TABLE user_agrees
    MODIFY third_party BIT (1) NOT NULL;

ALTER TABLE user_agrees
DROP
COLUMN marketing_agree;