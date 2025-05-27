CREATE INDEX idx_presbyopia_userid_createdat ON presbyopia_checks(user_id, created_at DESC);
CREATE INDEX idx_sight_userid_createdat ON sight_checks(user_id, created_at DESC);
CREATE INDEX idx_amsler_userid_createdat ON amsler_checks(user_id, created_at DESC);
CREATE INDEX idx_mchart_userid_createdat ON mchart_checks(user_id, created_at DESC);