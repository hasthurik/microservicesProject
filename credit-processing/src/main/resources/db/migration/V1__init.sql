CREATE TABLE product_registry (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    interest_rate DOUBLE PRECISION NOT NULL,
    open_date DATE NOT NULL
);

CREATE TABLE payment_registry (
    id BIGSERIAL PRIMARY KEY,
    product_registry_id BIGINT NOT NULL REFERENCES product_registry(id),
    payment_date DATE NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    interest_rate_amount NUMERIC(19,2) NOT NULL,
    debt_amount NUMERIC(19,2) NOT NULL,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    payment_expiration_date DATE NOT NULL
);
