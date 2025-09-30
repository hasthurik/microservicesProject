INSERT INTO users (login, password, email)
VALUES ('ivan123', 'pass123', 'ivan@example.com'),
       ('maria456', 'pass456', 'maria@example.com');

INSERT INTO clients (client_id, user_id, first_name, last_name, date_of_birth, type, document_id)
VALUES ('770100000001', 1, 'Иван', 'Иванов', '1990-05-10', 'PASSPORT', '123456'),
       ('770200000002', 2, 'Мария', 'Петрова', '1988-07-20', 'INT_PASSPORT', 'AA112233');

INSERT INTO products (name, key, create_date, product_id)
VALUES ('Дебетовая карта', 'DC', now(), 'DC1'),
       ('Кредитная карта', 'CC', now(), 'CC2');

INSERT INTO client_products (client_id, product_id, open_date, status)
VALUES ('770100000001', 'DC1', now(), 'ACTIVE'),
       ('770100000001', 'CC2', now(), 'ACTIVE');

INSERT INTO black_list (first_name, middle_name, last_name) VALUES
        ('Иван', 'Иванович', 'Иванов'),
        ('Петр', 'Петрович', 'Петров'),
        ('Сергей', 'Сергеевич', 'Сергеев'),
        ('Мария', 'Ивановна', 'Сидорова'),
        ('Анна', 'Петровна', 'Кузнецова'),
        ('Ольга', 'Сергеевна', 'Попова'),
        ('Алексей', 'Алексеевич', 'Васильев'),
        ('Дмитрий', 'Дмитриевич', 'Смирнов'),
        ('Екатерина', 'Викторовна', 'Морозова'),
        ('Николай', 'Николаевич', 'Волков');