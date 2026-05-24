-- Enable trigram extension
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Creating foreign key indexes for JOINs optimization
CREATE INDEX idx_products_brand_id ON products (brand_id);
CREATE INDEX idx_products_category_id ON products (category_id);
CREATE INDEX idx_products_model_id ON products (model_id);

-- Trigram indexes for product search using GIN from pg_trgm module
CREATE INDEX idx_products_name_trgm ON products USING GIN (LOWER(name) gin_trgm_ops);
CREATE INDEX idx_brands_name_trgm ON brands USING GIN (LOWER(name) gin_trgm_ops);
CREATE INDEX idx_categories_name_trgm ON categories USING GIN (LOWER(name) gin_trgm_ops);
CREATE INDEX idx_models_name_trgm ON models USING GIN (LOWER(name) gin_trgm_ops);

