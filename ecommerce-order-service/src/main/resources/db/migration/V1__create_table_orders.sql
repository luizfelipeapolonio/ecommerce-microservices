CREATE TABLE orders (
    id               UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    order_price      NUMERIC      NOT NULL,
    with_coupon      BOOLEAN      NOT NULL DEFAULT FALSE,
    coupon_code      VARCHAR(150),
    coupon_value     NUMERIC,
    shipping_fee     NUMERIC,
    checkout_url     TEXT,
    invoice_url      TEXT,
    customer_id      UUID         NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL
);