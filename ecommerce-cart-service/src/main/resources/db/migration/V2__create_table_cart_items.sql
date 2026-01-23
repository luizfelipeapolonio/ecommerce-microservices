CREATE TABLE cart_items (
    id              BIGSERIAL    NOT NULL PRIMARY KEY,
    product_id      UUID         NOT NULL,
    product_name    VARCHAR(300) NOT NULL,
    thumbnail_image TEXT         NOT NULL,
    unit_price      NUMERIC      NOT NULL,
    discount_type   VARCHAR(12),
    discount_value  VARCHAR(50),
    final_price     NUMERIC      NOT NULL,
    quantity        INTEGER      NOT NULL,
    cart_id         UUID         NOT NULL,
    added_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_carts FOREIGN KEY (cart_id) REFERENCES carts(id) ON UPDATE CASCADE ON DELETE CASCADE
);