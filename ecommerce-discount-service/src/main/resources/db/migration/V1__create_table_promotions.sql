CREATE TABLE promotions (
    id             UUID         NOT NULL PRIMARY KEY,
    name           VARCHAR(150) NOT NULL,
    description    VARCHAR(255),
    scope          VARCHAR(10)  NOT NULL,
    discount_type  VARCHAR(12)  NOT NULL CHECK (discount_type IN ('percentage', 'fixed_amount')),
    discount_value VARCHAR(50)  NOT NULL,
    minimum_price  NUMERIC      NOT NULL,
    end_date       TIMESTAMP    NOT NULL,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL
);