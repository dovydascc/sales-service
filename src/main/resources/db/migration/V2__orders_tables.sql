CREATE TABLE orders
(
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(64)  NOT NULL,
    address    VARCHAR(256) NOT NULL,
    ordered_on TIMESTAMP    NOT NULL,
    UNIQUE uk_orders_email_ordered_on (email, ordered_on)
) ENGINE = InnoDB;
CREATE INDEX ix_orders_email_ordered_on ON orders (email, ordered_on);

CREATE TABLE purchased_products
(
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT UNSIGNED NOT NULL,
    product_id BIGINT UNSIGNED NOT NULL,
    quantity   INT UNSIGNED    NOT NULL,
    price      DECIMAL(15, 2)  NOT NULL,
    CONSTRAINT fk_purchased_products_orders FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_purchased_products_products FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB;