--liquibase formatted sql

--changeset viktoryiaver:1
UPDATE order_details SET book_price = 10.79, book_quantity = 1 WHERE id = 1;
UPDATE order_details SET book_price = 20.22, book_quantity = 2 WHERE id = 2;
UPDATE order_details SET book_price = 40.23, book_quantity = 1 WHERE id = 3;
UPDATE order_details SET book_price = 30.00, book_quantity = 1 WHERE id = 4;
UPDATE order_details SET book_price = 150.23, book_quantity = 1 WHERE id = 5;
UPDATE order_details SET book_price = 7.53, book_quantity = 1 WHERE id = 6;
UPDATE order_details SET book_price = 19.10, book_quantity = 1 WHERE id = 7;