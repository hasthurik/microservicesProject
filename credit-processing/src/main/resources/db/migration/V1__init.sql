CREATE TABLE product_registry (
    id SERIAL PRIMARY KEY,
    client_id VARCHAR(12) NOT NULL,
    account_id BIGINT NOT NULL,
    producer_id BIGINT,
    interest_rate DOUBLE PRECISION,
    open_date DATE,
    payment_date TIMESTAMP,
    mount_number INT
);

CREATE TABLE payment_registry (
    id SERIAL PRIMARY KEY,
    product_registry_id BIGINT NOT NULL REFERENCES product_registry(id),
    payment_date DATE NOT NULL,
    amount NUMERIC(18,2),
    interest_rate_amount NUMERIC(18,2) NOT NULL,
    debt_amount NUMERIC(18,2) NOT NULL,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    payment_expiration_date DATE NOT NULL
    );
