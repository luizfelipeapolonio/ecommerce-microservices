CREATE TABLE mail_to (
    id            BIGSERIAL    NOT NULL PRIMARY KEY,
    order_id      UUID         NOT NULL,
    order_price   VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL,
    shipping_fee  VARCHAR(100) NOT NULL,
    coupon_value  VARCHAR(100),
    tracking_code TEXT,
    invoice_url   TEXT,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);