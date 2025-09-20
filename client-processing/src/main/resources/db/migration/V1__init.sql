CREATE TABLE users (
    id bigserial PRIMARY KEY,
    login VARCHAR(50) NOT NULL unique,
    password VARCHAR(50) NOT NULL,
    email  VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE clients (
    id bigserial PRIMARY KEY,
    client_id VARCHAR(20) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    document_type VARCHAR(20) NOT NULL,
    document_id VARCHAR(50) NOT NULL,
    document_prefix VARCHAR(20),
    document_suffix VARCHAR(20)
);

CREATE TABLE products (
    id bigserial PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_key VARCHAR(10) NOT NULL,
    create_date DATE NOT NULL,
    product_id VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE client_products (
    id bigserial PRIMARY KEY,
    client_id BIGSERIAL NOT NULL REFERENCES clients(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    open_date DATE NOT NULL,
    close_date DATE,
    status VARCHAR(20) NOT NULL
);