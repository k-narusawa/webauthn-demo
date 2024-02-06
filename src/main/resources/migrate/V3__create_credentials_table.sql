CREATE TABLE credentials
(
    credential_id                       VARCHAR(255) PRIMARY KEY NOT NULL,
    serialized_attested_credential_data VARCHAR(255)             NOT NULL,
    serialized_envelope                 VARCHAR(255)             NOT NULL,
    serialized_transports               VARCHAR(255)             NOT NULL,
    serialized_authenticator_extensions VARCHAR(255)             NOT NULL,
    serialized_client_extensions        VARCHAR(255)             NOT NULL,
    counter                             INT                      NOT NULL,
    created_at                          DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                          DATETIME                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);