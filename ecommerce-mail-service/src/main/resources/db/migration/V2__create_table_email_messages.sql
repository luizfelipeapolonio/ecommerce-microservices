CREATE TABLE email_messages (
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    subject    VARCHAR(255) NOT NULL,
    status     VARCHAR(30)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at    TIMESTAMP,
    mail_to_id BIGINT       NOT NULL,

    CONSTRAINT fk_mail_to FOREIGN KEY (mail_to_id) REFERENCES mail_to(id) ON UPDATE CASCADE ON DELETE CASCADE
);