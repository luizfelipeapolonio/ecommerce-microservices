CREATE TABLE shipments (
    id                  UUID         NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id            UUID         NOT NULL,
    status              VARCHAR(30)  NOT NULL,
    tracking_code       TEXT         NOT NULL UNIQUE,
    total_weight        VARCHAR(100) NOT NULL,
    shipped_at          TIMESTAMP,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL,
    delivered_at        TIMESTAMP,
    shipment_address    TEXT         NOT NULL
);