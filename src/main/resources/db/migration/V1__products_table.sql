CREATE TABLE products
(
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    sku           VARCHAR(16)    NOT NULL UNIQUE,
    name          VARCHAR(32)    NOT NULL,
    description   VARCHAR(256)   NOT NULL,
    price         DECIMAL(15, 2) NOT NULL,
    warranty_days INT  DEFAULT 0,
    listed        BOOL DEFAULT FALSE,
    UNIQUE uk_products_name_price (name, price)
) ENGINE = InnoDB;
CREATE INDEX ix_product_listed_name ON products (name, listed);