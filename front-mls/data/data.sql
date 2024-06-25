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

CREATE TABLE coordonnees_patient (
        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        structure_id INT NOT NULL,
        nom VARCHAR(30) NOT NULL,
        prenom VARCHAR(30) NOT NULL,
        date_de_naissance DATE NOT NULL,
        genre VARCHAR(1) NOT NULL,
        adresse VARCHAR(50),
        telephone VARCHAR(12),
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
    ('Joe', 'Louis','USER','joe.louis@test.fr',2,'$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Samantha', 'Doc','USER','sam.doc@test.fr',3,'$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Denis', 'La Malice','USER','denis.lm@test.fr',2,'$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32');

INSERT INTO coordonnees_patient(structure_id, nom, prenom, date_de_naissance, genre, adresse, telephone)
VALUES
    (2,'TESTNONE','TEST','1966-12-31','F','1 Brookside St','100-222-3333'),
    (3,'TESTBORDERLINE','TEST','1945-06-24','M','2 High St','200-333-4444'),
    (2,'TESTINDANGER','TEST','2004-06-18','M','3 Club Road','300-444-5555'),
    (3,'TESTEARLYONSET','TEST','2002-06-28','F','4 Valley Dr','400-555-6666');



