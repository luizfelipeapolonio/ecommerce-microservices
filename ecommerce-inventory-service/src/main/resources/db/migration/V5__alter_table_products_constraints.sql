ALTER TABLE products
    ADD CONSTRAINT fk_categories FOREIGN KEY (category_id) REFERENCES categories(id),
    ADD CONSTRAINT fk_brands     FOREIGN KEY (brand_id)    REFERENCES brands(id),
    ADD CONSTRAINT fk_models     FOREIGN KEY (model_id)    REFERENCES models(id);