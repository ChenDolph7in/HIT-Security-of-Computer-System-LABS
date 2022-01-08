DROP DATABASE IF EXISTS lab3;
CREATE DATABASE `lab3`;
USE lab3;

CREATE TABLE `user_info`
(
    id int PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) UNIQUE,
    currency int NOT NULL,
    valid tinyint(1) NOT NULL
);

CREATE TABLE `admin_info`
(
    id int PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) NOT NULL
);

CREATE TABLE `bill_info`
(
    bill_id int PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    change_money int NOT NULL
);

CREATE TABLE `audit_info`
(
    bill_id int PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) NOT NULL,
);

DROP USER if EXISTS 'general'@'localhost';
DROP USER if EXISTS 'admin'@'localhost';
DROP USER if EXISTS 'user1'@'localhost';
DROP USER if EXISTS 'user2'@'localhost';
DROP USER if EXISTS 'user3'@'localhost';

INSERT INTO `user_info` VALUES (1, 'user1', 0, true);
INSERT INTO `user_info` VALUES (2, 'user2', 0, true);
INSERT INTO `user_info` VALUES (3, 'user3', 0, true);
INSERT INTO `admin_info` VALUES (4,'admin');

FLUSH PRIVILEGES;

CREATE USER 'general'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'general';
CREATE USER 'admin'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'admin';
CREATE USER 'user1'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'user1';
CREATE USER 'user2'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'user2';
CREATE USER 'user3'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'user3';

GRANT select,update ON lab3.user_info to 'general'@'localhost' WITH GRANT OPTION;
GRANT select,delete ON lab3.bill_info to 'general'@'localhost' WITH GRANT OPTION;
GRANT select ON mysql.columns_priv to 'general'@'localhost' WITH GRANT OPTION;
GRANT insert(username), insert(change_money) ON lab3.bill_info TO 'general'@'localhost'WITH GRANT OPTION;
GRANT select,insert,delete ON lab3.admin_info to 'general'@'localhost';

GRANT select,update ON lab3.user_info to 'admin'@'localhost';
GRANT select,delete ON lab3.bill_info to 'admin'@'localhost';

GRANT select(currency), select(username), select(valid) ON lab3.user_info TO 'user1'@'localhost';
GRANT insert(username), insert(change_money) ON lab3.bill_info TO 'user1'@'localhost';
GRANT select(currency), select(username), select(valid) ON lab3.user_info TO 'user2'@'localhost';
GRANT insert(username), insert(change_money) ON lab3.bill_info TO 'user2'@'localhost';
GRANT select(currency), select(username), select(valid) ON lab3.user_info TO 'user3'@'localhost';
GRANT insert(username), insert(change_money) ON lab3.bill_info TO 'user3'@'localhost';


FLUSH PRIVILEGES;