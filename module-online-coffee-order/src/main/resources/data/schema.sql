SET foreign_key_checks = 0;

DROP TABLE IF EXISTS products;

CREATE TABLE products
(
    product_id  INT            NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(200) NULL,
    price       DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (product_id)
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    order_id    INT      NOT NULL AUTO_INCREMENT,
    customer_id INT      NOT NULL,
    ordered_at  DATETIME NOT NULL,
    PRIMARY KEY (order_id)
);

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items
(
    order_item_id  INT NOT NULL AUTO_INCREMENT,
    product_id     INT NOT NULL,
    order_id       INT NOT NULL,
    order_quantity INT NOT NULL,
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

DROP TABLE IF EXISTS customers;

CREATE TABLE customers
(
    customer_id  INT          NOT NULL AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    phone_number VARCHAR(100) NULL,
    address      VARCHAR(100) NOT NULL,
    PRIMARY KEY (customer_id)
);

DROP TABLE IF EXISTS stores;

CREATE TABLE stores
(
    store_id     INT          NOT NULL AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    address      VARCHAR(100) NOT NULL,
    phone_number VARCHAR(100) NOT NULL,
    open_at      VARCHAR(100) NOT NULL,
    close_at     VARCHAR(100) NOT NULL,
    PRIMARY KEY (store_id)
);

DROP TABLE IF EXISTS store_products;

CREATE TABLE store_products
(
    store_product_id INT NOT NULL AUTO_INCREMENT,
    store_id         INT NOT NULL,
    product_id       INT NOT NULL,
    stock_quantity   INT NOT NULL,
    PRIMARY KEY (store_product_id)
);
