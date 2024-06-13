DROP DATABASE IF EXISTS mls;
CREATE DATABASE mls;
USE mls;

CREATE TABLE structure (
                                        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        name VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE user (
                     id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                     firstname VARCHAR(30) NOT NULL,
                     lastname VARCHAR(30) NOT NULL,
                     role VARCHAR(5) NOT NULL,
                     email VARCHAR(30) NOT NULL UNIQUE,
                     structure_id INT NOT NULL,
                     password VARCHAR(100) NOT NULL,
                     FOREIGN KEY(structure_id) REFERENCES structure(id)
);

INSERT INTO structure (name)
VALUES
    ('mls'),
    ('labo analyse 1'),
    ('Cabinet Dr Sam');

INSERT INTO user (firstname, lastname, role, email, structure_id, password)
VALUES
    ('Phil', 'Dev','ADMIN','phil.dev@mls.fr',1,'$2a$10$zFvC9UAxeYJp3dnyv8cHHO0JupOZ6GtlGtAuWbCFzXn4znAShiGym'),
    ('Joe', 'Louis','USER','phil.louis@test.fr',2,'$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Samantha', 'Doc','USER','sam.doc@test.fr',3,'$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Denis', 'La Malice','USER','denis.lm@test.fr',2,'$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32');


