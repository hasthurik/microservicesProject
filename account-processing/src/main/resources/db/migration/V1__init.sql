CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    balance NUMERIC(19,2) NOT NULL DEFAULT 0,
    interest_rate DOUBLE PRECISION,
    is_recalc BOOLEAN NOT NULL DEFAULT FALSE,
    card_exist BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    card_id VARCHAR(50) UNIQUE NOT NULL,
    payment_system VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    payment_date DATE NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    is_credit BOOLEAN NOT NULL,
    payed_at TIMESTAMP,
    type VARCHAR(50) NOT NULL
);

CREATE TYPE transaction_status AS ENUM (
    'ALLOWED',
    'PROCESSING',
    'COMPLETE',
    'BLOCKED',
    'CANCELLED'
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    card_id BIGINT REFERENCES cards(id),
    transaction_type VARCHAR(50) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status transaction_status NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);