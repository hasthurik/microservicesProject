CREATE TABLE users (
    id bigserial PRIMARY KEY,
    login VARCHAR(50) NOT NULL unique,
    password VARCHAR(50) NOT NULL,
    email  VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE clients (
    id bigserial PRIMARY KEY,
    client_id VARCHAR(12) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    type VARCHAR(50) NOT NULL
        check (type in ('PASSPORT', 'INT_PASSPORT', 'BIRTH_CERT')),
    document_id VARCHAR(50),
    document_prefix VARCHAR(20),
    document_suffix VARCHAR(20)
);

CREATE TABLE products (
    id bigserial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    key VARCHAR(10) NOT NULL,
    create_date date NOT NULL,
    product_id VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE client_products (
    id bigserial PRIMARY KEY,
    client_id varchar(12)  NOT NULL REFERENCES clients(client_id) ON DELETE CASCADE,
    product_id VARCHAR(50) NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    open_date DATE NOT NULL,
    close_date DATE,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE black_list (
    id bigserial PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL
)