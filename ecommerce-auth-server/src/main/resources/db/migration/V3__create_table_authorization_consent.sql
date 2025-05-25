CREATE TABLE authorization_consent (
    registered_client_id VARCHAR(255)  NOT NULL,
    principal_name       VARCHAR(255)  NOT NULL,
    authorities          VARCHAR(1000) NOT NULL,

    PRIMARY KEY (registered_client_id, principal_name)
);