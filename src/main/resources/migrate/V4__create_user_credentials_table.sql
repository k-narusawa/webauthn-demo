CREATE TABLE user_credentials
(
    credential_id VARCHAR(255) PRIMARY KEY NOT NULL,
    user_id       VARCHAR(255)             NOT NULL,
    created_at    DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);