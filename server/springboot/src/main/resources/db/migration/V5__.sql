ALTER TABLE surveys
DROP
COLUMN surgery;

ALTER TABLE surveys
    ADD surgery VARCHAR(10) NOT NULL;