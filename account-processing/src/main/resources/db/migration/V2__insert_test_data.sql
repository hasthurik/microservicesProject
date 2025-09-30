INSERT INTO accounts (client_id, product_id, balance, interest_rate, is_recalc, card_exist, status)
VALUES
    ( '770100000001', 'DC1', 15000.00, 3.5, TRUE, TRUE, 'ACTIVE'),
    ( '770200000023', 'CC23', 2500.00, 2.0, FALSE, FALSE, 'BLOCKED'),
    ( '780100000045', 'AC45', 100000.00, 5.0, TRUE, TRUE, 'ACTIVE');

INSERT INTO cards (account_id, card_id, payment_system, status)
VALUES
    (1, 'CARD_001', 'VISA', 'ACTIVE'),
    (1, 'CARD_002', 'MASTERCARD', 'ACTIVE'),
    (2, 'CARD_003', 'VISA', 'BLOCKED');

INSERT INTO payments (account_id, payment_date, amount, is_credit, type)
VALUES
    (1, CURRENT_DATE - INTERVAL '10 days', 500.00, FALSE, 'TRANSFER'),
    (1, CURRENT_DATE - INTERVAL '5 days', 1200.00, TRUE, 'CREDIT_PAYMENT'),
    (2, CURRENT_DATE - INTERVAL '2 days', 300.00, FALSE, 'WITHDRAWAL');

INSERT INTO transactions (account_id, card_id, transaction_type, amount, status, timestamp)
VALUES
    (1, 1, 'PAYMENT', 500.00, 'COMPLETE', NOW() - INTERVAL '9 days'),
    (1, 2, 'REFUND', 1200.00, 'PROCESSING', NOW() - INTERVAL '4 days'),
    (2, 3, 'WITHDRAWAL', 300.00, 'BLOCKED', NOW() - INTERVAL '1 day'),
    (3, NULL, 'TRANSFER', 1000.00, 'ALLOWED', NOW());