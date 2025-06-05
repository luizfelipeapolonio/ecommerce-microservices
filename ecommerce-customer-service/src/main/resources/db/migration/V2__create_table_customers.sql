CREATE TABLE customers (
    id           UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    email        VARCHAR(255) NOT NULL UNIQUE,
    username     VARCHAR(100) NOT NULL,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    address_id   BIGINT,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL,

    CONSTRAINT fk_address FOREIGN KEY (address_id) references addresses(id) ON UPDATE CASCADE ON DELETE CASCADE
);