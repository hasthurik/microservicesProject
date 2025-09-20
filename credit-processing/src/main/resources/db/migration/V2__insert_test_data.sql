INSERT INTO product_registry (client_id, account_id, product_id, interest_rate, open_date) VALUES
    (1, 1001, 2001, 5.5, '2024-01-15'),
    (1, 1002, 2002, 7.2, '2024-02-20'),
    (2, 1003, 2003, 4.8, '2024-03-10'),
    (2, 1004, 2004, 6.3, '2024-04-05'),
    (3, 1005, 2005, 8.1, '2024-05-12'),
    (3, 1006, 2006, 3.9, '2024-06-18');

INSERT INTO payment_registry (product_registry_id, payment_date, amount, interest_rate_amount, debt_amount, expired, payment_expiration_date) VALUES
(1, '2024-02-15', 15000.00, 825.00, 14175.00, false, '2024-02-20'),
(1, '2024-03-15', 14175.00, 779.63, 13395.37, false, '2024-03-20'),
(1, '2024-04-15', 13395.37, 736.75, 12658.62, false, '2024-04-20');