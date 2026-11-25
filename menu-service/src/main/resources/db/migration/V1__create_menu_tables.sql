CREATE TABLE menu_categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_category_restaurant_name
        UNIQUE (restaurant_id, name),

    CONSTRAINT uk_category_id_restaurant
        UNIQUE (id, restaurant_id)
);


CREATE TABLE menu_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(12, 2) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_menu_item_category
        FOREIGN KEY (category_id, restaurant_id)
        REFERENCES menu_categories (id, restaurant_id),

    CONSTRAINT chk_menu_item_price
        CHECK (price > 0),

    INDEX idx_menu_items_restaurant_id (restaurant_id),
    INDEX idx_menu_items_category_restaurant
        (category_id, restaurant_id)
);