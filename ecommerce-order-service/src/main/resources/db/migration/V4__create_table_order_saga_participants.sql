CREATE TABLE order_saga_participants (
    id          BIGSERIAL   NOT NULL PRIMARY KEY,
    name        VARCHAR(20) NOT NULL,
    saga_id     UUID        NOT NULL,
    status      VARCHAR(10) NOT NULL,
    CONSTRAINT fk_saga FOREIGN KEY (saga_id) REFERENCES order_saga(id) ON UPDATE CASCADE ON DELETE CASCADE
);