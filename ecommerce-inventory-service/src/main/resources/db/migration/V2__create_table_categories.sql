CREATE TABLE categories(
    id                 BIGSERIAL    NOT NULL PRIMARY KEY,
    name               VARCHAR(100) NOT NULL UNIQUE,
    parent_category_id BIGINT       DEFAULT  NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL,

    CONSTRAINT fk_categories FOREIGN KEY (parent_category_id) REFERENCES categories(id) ON UPDATE CASCADE ON DELETE CASCADE
);