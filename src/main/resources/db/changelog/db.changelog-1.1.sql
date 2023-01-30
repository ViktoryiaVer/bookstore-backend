--liquibase formatted sql

--changeset viktoryiaver:1
INSERT INTO logins (username, password)
VALUES
('Ivan_Ivanov_111','$2a$12$vdV1GTsrL.JiBK.1xeo49ecULAFyWNs.WNmk/DzdNWJIByZKWdmVS'),
('John_Snowman', '$2a$12$vkVjA1e1xYOjRx4HcgQFBO/hK0QN3RFF0QdWXfqqxEaLCrT650rYK'),
('M_Patterson', '$2a$12$XQ7Pmiccvj48tMIM/Bg6Wec7wXZHUSe4bEbxnUIxE/WWn159BdCbO'),
('Marina_Petrova_333', '$2a$12$IzUVbLwguRgsxJJGd8j5JOwDXexInnMW2VU5oWd4QwvSx.V9Y/FuS'),
('Sergei_Sidorov_2022', '$2a$12$uI2IB3PpryEibc1CpF1Sce/rAaiORhTbQ0uTdRrXtX.m0rUxPJ1ai'),
('MariaBystrova23', '$2a$12$pHu5tN3KxDVnZlosj0rsIekx4Dkhem3VsfZnBGAO.uTBEjwTjofxK'),
('Pavel_Shery_New', '$2a$12$BeL5.FVJADLbNu083tTnS.ZcgMFwTdtf2mW9h57WjFzPRLSdBAprK'),
('Irene_Johnson', '$2a$12$ZW9rV/akrH3DLeFv0KgAcO6Sfac0A76VO0hCB94IiZ9b8GYLVPhXi'),
('John_Doe_Login_1', '$2a$12$vP.RzrZ2MwYyhZpYoOvK/edKWzynp8jS0VR/YRNx2obx1bQ5WK/Kq'),
('ErvinNewman', '$2a$12$0IsEoqYzfJZC.xhjT0qbV.wYDxvoamJlYsxi4/v3ztlrYIN85FQXa'),
('Julia_Schmidt_123', '$2a$12$.EUDe/Vx7xie00fPBCNFTO1H0w6G/W/IM0bQzgTcuXwCVWKgnnlra'),
('Hans_Dieter', '$2a$12$xd8JM2fFdHJwVEWP9iauzOJBcKtfsIF/xYsg7ZoDj48QrxC9AfAYq'),
('Viki_HUBER', '$2a$12$UfpYR2PXBW9qLZpJtNnnNuK7azDqMTWhLJ7THQm0L6hjitjcZrPtO'),
('Bars_Maria_1987', '$2a$12$W.427Jc6vZxS0j9jHWNc5eQJfMalxKKfNaJSo88AONNSX6yfe3CGK'),
('Julia_Anders', '$2a$12$Lr1kAcHCuy4EqWRS7xgC0.i5zR6nG1lG2ur5EaMEM6v7Y1Rj3GBeG'),
('admin', '$2a$12$4x2jWbgcljXwRnnNaIeaWOEi2zkBEF9Jn/g5l9Sq.4K.rSc.eV57u'),
('reader', '$2a$12$Z2vUeaE/vE69OXHHxRF8P.B/UPXMI6jnOVenF7TpgsP1eEgxGt3Wq');

--changeset viktoryiaver:2
INSERT INTO users (first_name, last_name, email, phone_number, user_role, login_id)
VALUES
    ('Ivan', 'Ivanov', 'ivanIvanov1997@mail.ru', '+375333564524', 'ADMIN', (SELECT id FROM logins WHERE username = 'Ivan_Ivanov_111')),
    ('John', 'Snowman', 'JohnSnowman@gmail.com', '+441612345678', 'ADMIN', (SELECT id FROM logins WHERE username = 'John_Snowman')),
    ('Mathew', 'Patterson', 'PattersonM12@gmail.com', '+441611122444', 'ADMIN', (SELECT id FROM logins WHERE username = 'M_Patterson')),
    ('Marina', 'Petrova','PetrovaMarina992@mail.ru', '+375291234321', 'USER', (SELECT id FROM logins WHERE username = 'Marina_Petrova_333')),
    ('Sergei', 'Sidorov', 'Sidorov12345Ser@gmail.com', '+375445672434', 'USER', (SELECT id FROM logins WHERE username = 'Sergei_Sidorov_2022')),
    ('Maria', 'Bystrova', 'BystrM67184@yahoo.com', '+48215369742', 'USER', (SELECT id FROM logins WHERE username = 'MariaBystrova23')),
    ('Pavel', 'Shery', 'PavelSh123@yahoo.com', '+48215343846', 'USER', (SELECT id FROM logins WHERE username = 'Pavel_Shery_New')),
    ('Irene', 'Johnson', 'IreneJohnson@gmail.com', '+441613947162', 'USER', (SELECT id FROM logins WHERE username = 'Irene_Johnson')),
    ('John', 'Doe', 'JohnDoe1I@yahoo.com', '+12346726835', 'USER', (SELECT id FROM logins WHERE username = 'John_Doe_Login_1')),
    ('Ervin', 'Newman', 'Newman1Ervin@online.de', '+492416743815', 'USER', (SELECT id FROM logins WHERE username = 'ErvinNewman')),
    ('Julia', 'Schmidt', 'SchmidtJulia@gmail.com', '+492416746734', 'USER', (SELECT id FROM logins WHERE username = 'Julia_Schmidt_123')),
    ('Hans', 'Dieter', 'DieterHans92@yahoo.com', '+492036748356', 'USER', (SELECT id FROM logins WHERE username = 'Hans_Dieter')),
    ('Victoria', 'Huber', 'VikiHub123@yahoo.com', '+48563842558', 'USER', (SELECT id FROM logins WHERE username = 'Viki_HUBER')),
    ('Maria', 'Bars', 'BarsMariaSt@list.ru', '+79112354657', 'USER', (SELECT id FROM logins WHERE username = 'Bars_Maria_1987')),
    ('Julia', 'Anders', 'AndersJulia25@gmail.com', '+492415624857', 'USER', (SELECT id FROM logins WHERE username = 'Julia_Anders')),
    ('Admin', 'Admin', 'admin@mail.ru', '+3759999999', 'ADMIN', (SELECT id FROM logins WHERE username = 'admin')),
    ('Reader', 'Reader', 'reader@mail.ru', '+3758888888', 'USER', (SELECT id FROM logins WHERE username = 'reader'));

--changeset viktoryiaver:3
INSERT INTO authors (first_name, last_name, birthdate)
VALUES
   ('Stephen', 'King', '1947-09-21'),
   ('J', 'Rowling', '1965-07-31'),
   ('J', 'Tolkien', '1892-01-03'),
   ('Gustave', 'Flaubert', '1821-12-12'),
   ('F. Scott', 'Fitzgerald', '1896-09-24'),
   ('William', 'Shakespeare', '1564-03-26'),
   ('Vladimir', 'Nabokov', '1899-03-22'),
   ('Fyodor', 'Dostoyevsky', '1821-11-11'),
   ('J. D.', 'Salinger', '1919-01-01'),
   ('Lewis', 'Carroll', '1832-01-27'),
   ('Franz', 'Kafka', '1883-07-03'),
   ('Albert', 'Camus', '1913-11-07'),
   ('George', 'Orwell', '1903-06-25'),
   ('Thomas', 'Mann', '1875-06-06'),
   ('Victor', 'Hugo', '1802-02-26');

--changeset viktoryiaver:4
INSERT INTO books (title, publisher, isbn, year_of_publication, cover, price)
VALUES
    ('Gwendy''s Final Task', 'Gallery Books', '1982191554', '2022', 'SOFT', '30.24'),
    ('Harry Potter and the Order of the Phoenix: The Illustrated Edition', 'Scholastic Inc', '054579143X', '2022', 'HARD', '40.67'),
    ('Harry Potter and the Sorcerer''s Stone: Minalima Edition', 'Scholastic Inc', '1338596705', '2020', 'HARD', '40.00'),
    ('Harry Potter and the Goblet of Fire: Illustrated Edition', 'Bloomsbury Children''s Books', '1408845679', '2019', 'HARD', '45.77'),
    ('The Lord Of The Rings: 50th Anniversary Edition', 'William Morrow', '0618517650', '2004', 'HARD', '150.23'),
    ('Madame Bovary', 'Penguin Classics', '0140449124', '2002', 'HARD', '20.22'),
    ('The Great Gatsby', 'Fingerprint! Publishing', '9390183529', '2020', 'HARD', '19.10'),
    ('Hamlet: Deluxe Edition (Illustrated)', 'Independently published', '979-8568971160', '2020', 'HARD', '10.20'),
    ('Lolita', 'Knopf Doubleday Publishing Group', '9780679723165', '1989', 'SOFT', '13.59'),
    ('Karamazov Brothers (Wordsworth Classics)', 'Wordsworth Editions Ltd', '9781840221862', '2010', 'SOFT', '10.79'),
    ('Crime and Punishment (Penguin Classics)', 'Penguin Classics', '0140449132', '2002', 'HARD', '16.50'),
    ('The Catcher in the Rye', 'Back Bay Books', '0275965074', '2001', 'SOFT', '7.53'),
    ('Alice''s Adventures in Wonderland (MinaLima Edition)', 'Harper Design', '0062936611', '2019', 'HARD', '14.38'),
    ('The Trial (Oxford World''s Classics)', 'Oxford University Press', '0199238294', '2009', 'SOFT', '10.80'),
    ('The Stranger', 'Independently published', '979-8847693264', '2022', 'SOFT', '10.23'),
    ('Nineteen Eighty-Four', 'Penguin Classics', '0241453518', '2021', 'HARD', '21.45'),
    ('The Magic Mountain', 'VINTAGE', '0749386428', '1996', 'HARD', '40.23'),
    ('Les Miserables (Signet Classics)', 'Signet', '045141943X', '2013', 'HARD', '30.00');

--changeset viktoryiaver:5
INSERT INTO books_authors (book_id, author_id)
VALUES
    ((SELECT b.id FROM books b WHERE b.title = 'Gwendy''s Final Task'), (SELECT a.id FROM authors a WHERE a.first_name = 'Stephen' AND a.last_name = 'King')),
    ((SELECT b.id FROM books b WHERE b.title = 'Harry Potter and the Order of the Phoenix: The Illustrated Edition'), (SELECT a.id FROM authors a WHERE a.first_name = 'J' AND a.last_name = 'Rowling')),
    ((SELECT b.id FROM books b WHERE b.title = 'Harry Potter and the Sorcerer''s Stone: Minalima Edition'), (SELECT a.id FROM authors a WHERE a.first_name = 'J' AND a.last_name = 'Rowling')),
    ((SELECT b.id FROM books b WHERE b.title = 'Harry Potter and the Goblet of Fire: Illustrated Edition'), (SELECT a.id FROM authors a WHERE a.first_name = 'J' AND a.last_name = 'Rowling')),
    ((SELECT b.id FROM books b WHERE b.title = 'The Lord Of The Rings: 50th Anniversary Edition'), (SELECT a.id FROM authors a WHERE a.first_name = 'J' AND a.last_name = 'Tolkien')),
    ((SELECT b.id FROM books b WHERE b.title = 'Madame Bovary'), (SELECT a.id FROM authors a WHERE a.first_name = 'Gustave' AND a.last_name = 'Flaubert')),
    ((SELECT b.id FROM books b WHERE b.title = 'The Great Gatsby'), (SELECT a.id FROM authors a WHERE a.first_name = 'F. Scott' AND a.last_name = 'Fitzgerald')),
    ((SELECT b.id FROM books b WHERE b.title = 'Hamlet: Deluxe Edition (Illustrated)'), (SELECT a.id FROM authors a WHERE a.first_name = 'William' AND a.last_name = 'Shakespeare')),
    ((SELECT b.id FROM books b WHERE b.title = 'Lolita'), (SELECT a.id FROM authors a WHERE a.first_name = 'Vladimir' AND a.last_name = 'Nabokov')),
    ((SELECT b.id FROM books b WHERE b.title = 'Karamazov Brothers (Wordsworth Classics)'), (SELECT a.id FROM authors a WHERE a.first_name = 'Fyodor' AND a.last_name = 'Dostoyevsky')),
    ((SELECT b.id FROM books b WHERE b.title = 'Crime and Punishment (Penguin Classics)'), (SELECT a.id FROM authors a WHERE a.first_name = 'Fyodor' AND a.last_name = 'Dostoyevsky')),
    ((SELECT b.id FROM books b WHERE b.title = 'The Catcher in the Rye'), (SELECT a.id FROM authors a WHERE a.first_name = 'J. D.' AND a.last_name = 'Salinger')),
    ((SELECT b.id FROM books b WHERE b.title = 'Alice''s Adventures in Wonderland (MinaLima Edition)'), (SELECT a.id FROM authors a WHERE a.first_name = 'Lewis' AND a.last_name = 'Carroll')),
    ((SELECT b.id FROM books b WHERE b.title = 'The Trial (Oxford World''s Classics)'), (SELECT a.id FROM authors a WHERE a.first_name = 'Franz' AND a.last_name = 'Kafka')),
    ((SELECT b.id FROM books b WHERE b.title = 'The Stranger'), (SELECT a.id FROM authors a WHERE a.first_name = 'Albert' AND a.last_name = 'Camus')),
    ((SELECT b.id FROM books b WHERE b.title = 'Nineteen Eighty-Four'), (SELECT a.id FROM authors a WHERE a.first_name = 'George' AND a.last_name = 'Orwell')),
    ((SELECT b.id FROM books b WHERE b.title = 'The Magic Mountain'), (SELECT a.id FROM authors a WHERE a.first_name = 'Thomas' AND a.last_name = 'Mann')),
    ((SELECT b.id FROM books b WHERE b.title = 'Les Miserables (Signet Classics)'), (SELECT a.id FROM authors a WHERE a.first_name = 'Victor' AND a.last_name = 'Hugo'));

--changeset viktoryiaver:6
INSERT INTO orders (user_id, total_cost, status)
VALUES
    ((SELECT u.id FROM users u WHERE u.email = 'IreneJohnson@gmail.com'), 10.79, 'AWAITING_PAYMENT'),
    ((SELECT u.id FROM users u WHERE u.email = 'Newman1Ervin@online.de'), 40.44, 'PAYED'),
    ((SELECT u.id FROM users u WHERE u.email = 'VikiHub123@yahoo.com'), 70.23, 'PENDING'),
    ((SELECT u.id FROM users u WHERE u.email = 'SchmidtJulia@gmail.com'), 150.23, 'PAYED'),
    ((SELECT u.id FROM users u WHERE u.email = 'BarsMariaSt@list.ru'), 7.53, 'CANCELED'),
    ((SELECT u.id FROM users u WHERE u.email = 'AndersJulia25@gmail.com'), 19.10, 'COMPLETED');

--changeset viktoryiaver:7
INSERT INTO order_details (order_id, book_id, book_price, book_quantity)
VALUES
    (1, (SELECT b.id FROM books b WHERE b.title = 'Karamazov Brothers (Wordsworth Classics)'), 1, 10.79),
    (2, (SELECT b.id FROM books b WHERE b.title = 'Madame Bovary'), 2, 20.22),
    (3, (SELECT b.id FROM books b WHERE b.title = 'The Magic Mountain'), 1, 40.23),
    (3, (SELECT b.id FROM books b WHERE b.title = 'Les Miserables (Signet Classics)'), 1, 30.00),
    (4, (SELECT b.id FROM books b WHERE b.title = 'The Lord Of The Rings: 50th Anniversary Edition'), 1, 150.23),
    (5, (SELECT b.id FROM books b WHERE b.title = 'The Catcher in the Rye'), 1, 7.53),
    (6, (SELECT b.id FROM books b WHERE b.title = 'The Great Gatsby'), 1, 19.10);

--changeset viktoryiaver:8
INSERT INTO payments (user_id, order_id, payment_time, payment_method)
VALUES
    ((SELECT u.id FROM users u WHERE u.email = 'Newman1Ervin@online.de'), 2, '2022-09-10 15:00', 'CREDIT_OR_DEBIT_CARD'),
    ((SELECT u.id FROM users u WHERE u.email = 'SchmidtJulia@gmail.com'), 4, '2022-09-10 15:00', 'ONLINE_PAYMENT_SERVICE');