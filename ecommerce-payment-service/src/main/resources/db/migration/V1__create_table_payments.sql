CREATE TABLE payments (
    id           UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id     UUID         NOT NULL,
    order_amount NUMERIC      NOT NULL,
    customer_id  UUID         NOT NULL,
    checkout_id  VARCHAR(255) NOT NULL,
    status       VARCHAR(15)  NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL
);