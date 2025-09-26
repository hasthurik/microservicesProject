
CREATE TABLE accounts (
    id bigserial PRIMARY KEY,
    client_id VARCHAR(12) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    balance NUMERIC(19,2) DEFAULT 0.0,
    interest_rate DOUBLE PRECISION,
    is_recalc BOOLEAN NOT NULL DEFAULT FALSE,
    card_exist BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(50) NOT NULL
        check (status in ('ACTIVE', 'CLOSED', 'BLOCKED', 'ARRESTED'))
);

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGSERIAL NOT NULL REFERENCES accounts(id),
    card_id VARCHAR(50) UNIQUE NOT NULL,
    payment_system VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGSERIAL NOT NULL REFERENCES accounts(id),
    payment_date DATE NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    is_credit BOOLEAN NOT NULL,
    payed_at TIMESTAMP,
    type VARCHAR(50) NOT NULL
);


CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGSERIAL NOT NULL REFERENCES accounts(id),
    card_id BIGINT REFERENCES cards(id),
    transaction_type VARCHAR(50) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL
        check (status in ('ALLOWED', 'PROCESSING', 'COMPLETE', 'BLOCKED', 'CANCELLED')),
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);