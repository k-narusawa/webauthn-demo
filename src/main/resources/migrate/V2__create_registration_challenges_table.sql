CREATE TABLE registration_challenges
(
    flow_id    VARCHAR(255) PRIMARY KEY NOT NULL,
    user_id    VARCHAR(255)             NOT NULL,
    challenge  VARCHAR(255)             NOT NULL,
    expired_at DATETIME                 NOT NULL,
    created_at DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);