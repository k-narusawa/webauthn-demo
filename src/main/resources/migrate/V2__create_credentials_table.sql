CREATE TABLE credentials
(
    credential_id                VARCHAR(255) PRIMARY KEY NOT NULL,
    user_id                      VARCHAR(255)             NOT NULL,
    aaguid                       VARCHAR(255)             NOT NULL,
    label                        VARCHAR(255)             NOT NULL,
    attested_credential_data     VARCHAR(255)             NOT NULL,
    attestation_statement_format VARCHAR(255)             NOT NULL,
    transports                   VARCHAR(255)             NOT NULL,
    authenticator_extensions     VARCHAR(255)             NOT NULL,
    client_extensions            VARCHAR(255)             NOT NULL,
    counter                      INT                      NOT NULL,
    created_at                   DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                    DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);