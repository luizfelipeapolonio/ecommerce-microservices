CREATE TABLE coupons (
    id             UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR(150) NOT NULL,
    description    VARCHAR(255),
    coupon_code    VARCHAR(30)  NOT NULL UNIQUE,
    discount_type  VARCHAR(12)  NOT NULL CHECK (discount_type IN ('percentage', 'fixed_amount')),
    discount_value VARCHAR(50)  NOT NULL,
    minimum_price  NUMERIC      NOT NULL,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    end_date       TIMESTAMP    NOT NULL,
    usage_limit    INTEGER      NOT NULL,
    usage_count    INTEGER      NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL
);