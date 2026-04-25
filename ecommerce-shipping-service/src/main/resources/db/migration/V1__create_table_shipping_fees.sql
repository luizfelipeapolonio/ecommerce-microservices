CREATE TABLE shipping_fees (
    id       BIGSERIAL   NOT NULL PRIMARY KEY,
    distance VARCHAR(10) NOT NULL,
    weight   VARCHAR(15) NOT NULL,
    price    NUMERIC     NOT NULL
);