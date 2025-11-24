ALTER TABLE products
    ADD COLUMN with_discount  BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN promotion_id   VARCHAR(255),
    ADD COLUMN discount_type  VARCHAR(12),
    ADD COLUMN discount_value VARCHAR(50);
