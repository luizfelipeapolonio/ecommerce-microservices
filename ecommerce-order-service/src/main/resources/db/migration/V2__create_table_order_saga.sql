CREATE TABLE order_saga (
    id             UUID        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id       UUID        NOT NULL,
    status         VARCHAR(30) NOT NULL,
    failure_code   VARCHAR(30),
    failure_reason VARCHAR(255),
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP   NOT NULL
);