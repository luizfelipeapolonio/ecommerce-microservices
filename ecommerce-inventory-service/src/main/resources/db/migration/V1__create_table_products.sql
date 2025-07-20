CREATE TABLE products(
    id          UUID          NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(300)  NOT NULL,
    description VARCHAR(255)  NOT NULL,
    image_url   TEXT          NOT NULL,
    price_unity NUMERIC       NOT NULL,
    quantity    BIGINT        NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL,
    category_id BIGINT        NOT NULL,
    brand_id    BIGINT        NOT NULL,
    model_id    BIGINT        NOT NULL
);