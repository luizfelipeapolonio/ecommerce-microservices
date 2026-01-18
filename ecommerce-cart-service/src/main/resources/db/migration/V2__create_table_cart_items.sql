CREATE TABLE cart_items (
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    product_id UUID      NOT NULL,
    cart_id    UUID      NOT NULL,
    quantity   INTEGER   NOT NULL,
    added_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_carts FOREIGN KEY (cart_id) REFERENCES carts(id) ON UPDATE CASCADE ON DELETE CASCADE
);