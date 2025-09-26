
INSERT INTO product_registry (client_id, account_id, producer_id, interest_rate, open_date, payment_date, mount_number)
VALUES
    (770100000001, 1, 101, 22.0, '2025-01-01', '2025-02-01 10:00:00', 60),
    (770100000002, 2, 102, 18.0, '2025-03-15', '2025-04-15 10:00:00', 36),
    (770200000003, 3, 103, 20.0, '2025-05-10', '2025-06-10 10:00:00', 24);


INSERT INTO payment_registry (product_registry_id, payment_date, amount, interest_rate_amount, debt_amount, expired, payment_expiration_date)
VALUES
    (1, '2025-02-01', 42287.00, 27450.00, 14837.00, FALSE, '2025-02-28'),
    (1, '2025-03-01', 42287.00, 27000.00, 15287.00, FALSE, '2025-03-31'),
    (2, '2025-04-15', 30500.00, 21000.00, 9500.00, FALSE, '2025-05-15'),
    (3, '2025-06-10', 18000.00, 12000.00, 6000.00, FALSE, '2025-07-10');
