CREATE TABLE promotion_applies_to (
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    target       VARCHAR(10)  NOT NULL CHECK (target IN ('product', 'category', 'brand', 'model')),
    target_id    VARCHAR(255) NOT NULL,
    applied_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    promotion_id UUID         NOT NULL,

    CONSTRAINT fk_promotions FOREIGN KEY (promotion_id) REFERENCES promotions(id) ON UPDATE CASCADE ON DELETE CASCADE
);