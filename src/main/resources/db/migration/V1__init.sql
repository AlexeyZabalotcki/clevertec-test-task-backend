DROP TABLE IF EXISTS PRODUCTS;
CREATE TABLE PRODUCTS
(
    id       BIGINT AUTO_INCREMENT,
    title    varchar(30),
    producer varchar(30),
    price    decimal(30),
    discount boolean
);

DROP TABLE IF EXISTS DISCOUNT_CARD;
CREATE TABLE DISCOUNT_CARD
(
    id       BIGINT AUTO_INCREMENT,
    number   int,
    producer varchar(30),
    color    varchar(30),
    discount boolean
);

