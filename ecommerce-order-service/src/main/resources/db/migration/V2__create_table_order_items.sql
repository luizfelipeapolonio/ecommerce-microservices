CREATE TABLE order_items (
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    product_id   UUID         NOT NULL,
    product_name VARCHAR(300) NOT NULL,
    quantity     BIGINT       NOT NULL,
    final_price  NUMERIC      NOT NULL,
    order_id     UUID         NOT NULL,
    added_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders FOREIGN KEY (order_id) REFERENCES orders(id) ON UPDATE CASCADE ON DELETE CASCADE
);