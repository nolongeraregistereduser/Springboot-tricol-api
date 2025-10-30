--liquibase formatted sql

-- changeset tricol:1
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    raison_sociale VARCHAR(150) NOT NULL,
    address TEXT,
    city VARCHAR(100),
    ice VARCHAR(50),
    contact_person VARCHAR(100),
    email VARCHAR(120),
    phone VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


-- changeset tricol:2
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);


-- changeset tricol:3
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    unit_price DECIMAL(12,2) NOT NULL,
    category_id BIGINT,
    current_stock DECIMAL(12,3) DEFAULT 0,
    reorder_point DECIMAL(12,3) DEFAULT 0,
    unit_of_measure VARCHAR(50),
    minimum_threshold DECIMAL(12,3) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);


-- changeset tricol:4
CREATE TABLE supplier_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT,
    order_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    total_amount DECIMAL(14,2) DEFAULT 0,
    reception_date DATE,
    comments TEXT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE,
    CONSTRAINT chk_status CHECK (status IN ('PENDING','APPROVED','DELIVERED','CANCELLED'))
);


-- changeset tricol:5
CREATE TABLE supplier_order_lines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity DECIMAL(12,3) NOT NULL,
    unit_purchase_price DECIMAL(12,3) NOT NULL,
    line_total DECIMAL(14,2) AS (quantity * unit_purchase_price) STORED,
    FOREIGN KEY (order_id) REFERENCES supplier_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


-- changeset tricol:6
CREATE TABLE stock_batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    batch_number VARCHAR(100) UNIQUE NOT NULL,
    entry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    initial_quantity DECIMAL(12,3) NOT NULL,
    remaining_quantity DECIMAL(12,3) NOT NULL,
    unit_purchase_price DECIMAL(12,3) NOT NULL,
    supplier_order_id BIGINT,
    CONSTRAINT chk_quantities CHECK (remaining_quantity >= 0),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_order_id) REFERENCES supplier_orders(id)
);


-- changeset tricol:7
CREATE TABLE stock_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    batch_id BIGINT,
    movement_type VARCHAR(10),
    quantity DECIMAL(12,3) NOT NULL,
    movement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(100),
    source_reference BIGINT,
    comments TEXT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (batch_id) REFERENCES stock_batches(id),
    CONSTRAINT chk_movement_type CHECK (movement_type IN ('IN','OUT'))
);


-- changeset tricol:8
CREATE TABLE delivery_notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    note_number VARCHAR(50) UNIQUE NOT NULL,
    delivery_date DATE NOT NULL,
    receiving_department VARCHAR(100) NOT NULL,
    delivery_reason VARCHAR(20) DEFAULT 'PRODUCTION',
    status VARCHAR(20) DEFAULT 'DRAFT',
    comments TEXT,
    CONSTRAINT chk_delivery_reason CHECK (delivery_reason IN ('PRODUCTION','MAINTENANCE','OTHER')),
    CONSTRAINT chk_delivery_status CHECK (status IN ('DRAFT','APPROVED','CANCELLED'))
);


-- changeset tricol:9
CREATE TABLE delivery_note_lines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    delivery_note_id BIGINT,
    product_id BIGINT,
    quantity DECIMAL(12,3) NOT NULL,
    FOREIGN KEY (delivery_note_id) REFERENCES delivery_notes(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);