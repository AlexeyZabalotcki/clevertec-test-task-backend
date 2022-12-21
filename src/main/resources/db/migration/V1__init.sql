CREATE TABLE products
(
    id       serial primary key,
    title    varchar(30),
    price    decimal(30),
    discount smallint
);

INSERT INTO products
VALUES (1, 'product 1', 50, 1),
       (2, 'product 2', 10, 0),
       (3, 'product 3', 3, 1),
       (4, 'product 4', 45, 0),
       (5, 'product 5', 76, 1),
       (6, 'product 6', 3, 0),
       (7, 'product 7', 100, 1),
       (8, 'product 8', 432, 0),
       (9, 'product 9', 5, 1),
       (10, 'product 10', 43, 0),
       (11, 'product 11', 87, 1),
       (12, 'product 12', 42, 0);

CREATE TABLE discount_card
(
    id       serial primary key,
    number   int,
    discount smallint
);

INSERT INTO discount_card
VALUES (1, 1111, 1),
       (2, 1112, 1),
       (3, 1113, 1),
       (4, 1114, 1),
       (5, 1115, 1),
       (6, 1116, 1);