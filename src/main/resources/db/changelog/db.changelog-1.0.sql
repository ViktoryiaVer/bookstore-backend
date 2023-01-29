--liquibase formatted sql

--changeset viktoryiaver:1
CREATE TABLE IF NOT EXISTS logins (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
--rollback DROP TABLE logins;

--changeset viktoryiaver:2
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(255) UNIQUE NOT NULL,
    user_role VARCHAR(70) NOT NULL,
    login_id BIGINT REFERENCES logins
);
--rollback DROP TABLE users;

--changeset viktoryiaver:3
CREATE TABLE IF NOT EXISTS authors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL
);
--rollback DROP TABLE authors;

--changeset viktoryiaver:4
CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) UNIQUE NOT NULL,
    year_of_publication INT NOT NULL,
    cover VARCHAR(30) NOT NULL,
    price DECIMAL(8, 2) NOT NULL
);
--rollback DROP TABLE books;

--changeset viktoryiaver:5
CREATE TABLE IF NOT EXISTS books_authors (
    book_id BIGINT REFERENCES books,
    author_id BIGINT REFERENCES authors,
    PRIMARY KEY(book_id, author_id)
);
--rollback DROP TABLE books_authors;

--changeset viktoryiaver:6
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users,
    total_cost DECIMAl(8, 2) NOT NULL,
    status VARCHAR(30) NOT NULL
);
--rollback DROP TABLE orders;

--changeset viktoryiaver:7
CREATE TABLE IF NOT EXISTS order_details (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders,
    book_id BIGINT NOT NULL REFERENCES books,
    book_price DECIMAl(8, 2) NOT NULL,
    book_quantity INT NOT NULL
);
--rollback DROP TABLE order_details;

--changeset viktoryiaver:8
CREATE TABLE  IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users,
    order_id BIGINT NOT NULL REFERENCES orders,
    payment_time TIMESTAMP NOT NULL,
    payment_method VARCHAR(50) NOT NULL
);
--rollback DROP TABLE payments;