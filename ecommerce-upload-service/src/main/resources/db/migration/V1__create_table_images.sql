CREATE TABLE images(
    id                 UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    name               VARCHAR(255) NOT NULL,
    path               TEXT         NOT NULL,
    file_type          VARCHAR(50)  NOT NULL,
    file_size          BIGINT       NOT NULL,
    original_file_name TEXT         NOT NULL,
    image_for          VARCHAR(10)  NOT NULL,
    product_id         UUID         NOT NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL
);