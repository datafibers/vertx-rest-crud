CREATE DATABASE IF NOT EXISTS whisky_db CHARSET utf8;

DROP TABLE IF EXISTS whisky;

CREATE TABLE whisky (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255),
    origin VARCHAR(255),
    PRIMARY KEY (id)
);

INSERT INTO whisky (id, name) VALUES (1, 'King Arthur');
INSERT INTO whisky (id, name) VALUES (2, 'John Walker');
INSERT INTO whisky (id, name) VALUES (3, 'Beefeater');
