CREATE TABLE coupon_applied_by (
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    order_id    UUID      NOT NULL UNIQUE,
    customer_id UUID      NOT NULL,
    coupon_id   UUID      NOT NULL,
    applied_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_coupons FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON UPDATE CASCADE ON DELETE CASCADE
);