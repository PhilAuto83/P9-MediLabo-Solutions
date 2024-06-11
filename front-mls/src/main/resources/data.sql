DROP DATABASE IF EXISTS mls;
CREATE DATABASE mls;
USE mls;
CREATE TABLE user(
                     id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                     firstname VARCHAR(30) NOT NULL,
                     lastname VARCHAR(30) NOT NULL,
                     role VARCHAR(5) NOT NULL,
                     email VARCHAR(30) NOT NULL UNIQUE,
                     societe VARCHAR(30) NOT NULL,
                     password VARCHAR(100) NOT NULL
);

INSERT INTO user (firstname, lastname, role, email, societe, password)
VALUES
    ('Phil', 'Dev','ADMIN','phil.dev@mls.fr','mls','$2a$10$zFvC9UAxeYJp3dnyv8cHHO0JupOZ6GtlGtAuWbCFzXn4znAShiGym'),
    ('Joe', 'Louis','USER','phil.louis@test.fr','labo analyse 1','$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Samantha', 'Doc','USER','sam.doc@test.fr','Cabinet docteur Sam','$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32');
