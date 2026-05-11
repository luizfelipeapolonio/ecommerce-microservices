CREATE TABLE order_items (
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    quantity   BIGINT       NOT NULL,
    name       VARCHAR(300) NOT NULL,
    price      VARCHAR(100) NOT NULL,
    mail_to_id BIGINT       NOT NULL,

    CONSTRAINT fk_mail_to FOREIGN KEY (mail_to_id) REFERENCES mail_to(id) ON UPDATE CASCADE ON DELETE CASCADE
);