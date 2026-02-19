CREATE TABLE orders (
    id               UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id       UUID         NOT NULL,
    product_name     VARCHAR(300) NOT NULL,
    product_quantity INTEGER      NOT NULL,
    final_price      NUMERIC      NOT NULL,
    with_coupon      BOOLEAN      NOT NULL DEFAULT FALSE,
    coupon_id        UUID,
    customer_id      UUID         NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);