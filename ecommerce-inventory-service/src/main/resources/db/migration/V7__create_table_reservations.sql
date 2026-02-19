CREATE TABLE reservations (
    id         BIGSERIAL   NOT NULL PRIMARY KEY,
    product_id UUID        NOT NULL,
    order_id   UUID        NOT NULL UNIQUE,
    quantity   INTEGER      NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL
);