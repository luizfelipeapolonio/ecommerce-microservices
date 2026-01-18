CREATE TABLE carts (
    id          UUID      NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID      NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);